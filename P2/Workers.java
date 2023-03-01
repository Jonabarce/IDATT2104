import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Workers extends Thread{
    private List<Runnable> tasks;
    private final Lock tasksLock;
    private final Condition tasksCondition;
    private final List<Thread> workerThreads;
    private volatile boolean isRunning;
    private int totalThreads;


    @Override
    public void run() {
        System.out.println("Running tasks in worker threads");
        runTasksInWorkerThreads();
    }

    public Workers(int totalThreads) {
        tasks = new LinkedList<>();
        tasksLock = new ReentrantLock();
        tasksCondition = tasksLock.newCondition();
        workerThreads = new LinkedList<>();
        isRunning = true;
        this.totalThreads = totalThreads;
    }

    public void post(Runnable task) {
        tasksLock.lock();
        try {
            tasks.add(task);
            tasksCondition.signal();
            System.out.println("Giving signal to all");
        } finally {
            tasksLock.unlock();
        }
    }

    public void postTimeout(Runnable task, long timeout) throws InterruptedException {
        Thread.sleep(timeout);
        post(task);
    }

    public void postTasks(Workers workers) {
        for (int i = 0; i < 10; i++) {
            int taskNum = i;
            workers.post(() -> {
                System.out.println("Task " + taskNum + " running in thread " + Thread.currentThread().getId());
            });
        }
    }
    public void runTasksInWorkerThreads() {
        System.out.println("hei jonatan");
        for (int i = 0; i < totalThreads; i++) {
            workerThreads.add(new Thread(() -> {
                while (isRunning) {
                    Runnable task = null;
                    tasksLock.lock();
                    try {
                        while (tasks.isEmpty() && isRunning) {
                            tasksCondition.await();
                        }
                        if (!tasks.isEmpty()) {
                            task = tasks.remove(0);
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Worker thread interrupted");
                        Thread.currentThread().interrupt();
                    } finally {
                        tasksLock.unlock();
                    }
                    if (task != null) {
                        try {
                            task.run();
                        } catch (Exception e) {
                            System.out.println("Error running task");
                            e.printStackTrace();
                        }
                    }
                }
            }));
        }
        for (Thread workerThread : workerThreads) {
            workerThread.start();
        }
    }


    public void stopAndFinish() {
        isRunning = false;
        tasksLock.lock();
        try {
            tasksCondition.signalAll();
        } finally {
            tasksLock.unlock();
        }
        for (Thread workerThread : workerThreads) {
            try {
                workerThread.join();
            } catch (InterruptedException e) {
                System.out.println("Error joining worker thread");
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {
        Workers workers = new Workers(4);
        workers.start();
        workers.postTasks(workers);
    }
}
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrimeFinder extends Thread{

    private int start;
    private int end;
    private List<Integer> primes = new ArrayList<>();

    public PrimeFinder(int start, int end){
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        List<Integer> subList = generateList(start,end);
        checkForPrime(subList);
    }

    public List<Integer> generateList (int start, int end){
        List<Integer> listOfNumbers;
        listOfNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        return listOfNumbers;
    }


    public boolean isPrime(int number) {
        for (int i = 0; i <= number; i++) {
            BigInteger bi = BigInteger.valueOf(i);
            if (bi.isProbablePrime(1000*number)) {
                return true;
            }
        }
        return false;
    }

/*
    public boolean isPrime(int number) {
        for (int i = 2; i <= number / 2; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
*/

    public void checkForPrime(List<Integer> listOfNumbers){
        for (Integer number : listOfNumbers) {
            if (isPrime(number)) {
                primes.add(number);
            }
        }
    }


    public List<Integer> sortList(List<Integer> listOfPrimes){
        Collections.sort(listOfPrimes);
        return listOfPrimes;
    }


    public List<Thread> generateListWithThreads(int totalThreads,int start,int end){
        List<Thread> threads = new ArrayList<>();
        int range = (end - start) / totalThreads;
        int counter = 0;
        for(int i = 0; i < totalThreads; i++){
            PrimeFinder object = new PrimeFinder(start+counter, start+counter+range-1);
            threads.add(object);
            counter += range;
        }
        return threads;
    }


    public void startThreads(List<Thread> threads){
        for(int i = 0; i < threads.size(); i++){
            threads.get(i).start();
        }
    }


    public static void threadJoiner(List<Thread> threads){
        for(Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Integer> getPrimes() {
        return primes;
    }

    public static String takeTime(int start, int end, int totalThreads){
        PrimeFinder taskOne = new PrimeFinder(start, end);
        List<Thread> threads;
        Date startTime = new Date();
        int rounds = 0;
        double time;
        Date endTime;
        do {
            threads = taskOne.generateListWithThreads(totalThreads,start,end);
            taskOne.startThreads(threads);
            threadJoiner(threads);
            List<Integer> finalPrimes = new ArrayList<>();
            for(Thread thread : threads){
                PrimeFinder primeFinder = (PrimeFinder) thread;
                finalPrimes.addAll(primeFinder.getPrimes());
            }
            taskOne.sortList(finalPrimes);
            System.out.println(finalPrimes);
            endTime = new Date();
            ++rounds;
        } while (endTime.getTime()-startTime.getTime() < 1);
        time = (double) (endTime.getTime()-startTime.getTime()) / rounds;

        return "" + time;
    }

    public static void main(String[] args) {
        System.out.println(takeTime(1,10000000,16));
    }
}
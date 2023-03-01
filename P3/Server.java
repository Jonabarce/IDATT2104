import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    static class ClientHandler extends Thread {

        public Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String num1String = in.readLine();
                String num2String = in.readLine();
                String operator = in.readLine();

                System.out.println("Received values from client: num1=" + num1String + ", num2=" + num2String + ", operator=" + operator);


                int num1 = Integer.parseInt(num1String);
                int num2 = Integer.parseInt(num2String);
                int result = 0;
                if (operator.equals("+")) {
                    result = num1 + num2;
                } else if (operator.equals("-")) {
                    result = num1 - num2;
                } else {
                    out.println("Invalid Operator");
                }
                out.println(result);
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(999);
        System.out.println("Server started on port 999");

        while (true) {
            // Venter på en klient tilkobling
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            // Start en ny tråd for hver klient
            new ClientHandler(clientSocket).start();
        }
    }
}
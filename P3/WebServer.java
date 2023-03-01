import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(999);
        System.out.println("Web server is waiting for incoming connections...");
        Socket client = server.accept();
        System.out.println("Client connected");
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        String line;
        StringBuilder header = new StringBuilder();
        while ((line = in.readLine()).length() != 0) {
            header.append(line).append("\n");
        }

        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println();
        out.println("<HTML><BODY>");
        out.println("<H1> Welcome! You have connected to my simple web server </h1>");
        out.println("Header from client is:");
        out.println("<UL>");
        Scanner headerScanner = new Scanner(header.toString());
        while (headerScanner.hasNextLine()) {
            out.println("<LI>" + headerScanner.nextLine() + "</LI>");
        }
        out.println("</UL>");
        out.println("</BODY></HTML>");
        client.close();
    }
}
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;


    //    constructor
    public Server() {
        try {
            server = new ServerSocket(9000);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //    Multi-threading concept ----- to read data from client
    public void startReading() {

        Runnable r1 = () -> {
            System.out.println("Reader started...");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client : " + msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        new Thread(r1).start();

    }

    //    Multi-threading concept ----- to Write data for client
    public void startWriting() {

        Runnable r2 = () -> {
            System.out.println("Writer Started...");
            while (!socket.isClosed()) {
                try {
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br2.readLine();
                    System.out.println("Server : " + content);
                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();

    }


    public static void main(String[] args) {
        System.out.println("This is server.. going to start ");
        new Server();
    }
}

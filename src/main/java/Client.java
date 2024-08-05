import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    Socket socket;
    ServerSocket sc;
    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {

            System.out.println("Sending Request To Server...");
            socket = new Socket("127.0.0.1", 9000);
            System.out.println("Connection Done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {

        Runnable r1 = () -> {
            System.out.println("Reader started...");
            try {
                while (!socket.isClosed()) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server : " + msg);
                }

            } catch (IOException e) {
                System.out.println("");;
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
                    if (content.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        break;
                    }
                    System.out.println("Client : " + content);
                    out.println(content);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();

    }


    public static void main(String[] args) {
        System.out.println();
        new Client();
    }

}

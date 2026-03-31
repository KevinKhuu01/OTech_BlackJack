package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private String username;

    public Client(String host, int port, String username) {
        this.username = username;

        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to server.");
        } catch (IOException e) {
            System.out.println("Error connecting to server.");
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            String serverMessage = input.readLine();
            System.out.println(serverMessage);

            while (socket != null && !socket.isClosed()) {
                String message = keyboard.readLine();
                output.println(message);

                if (message.equalsIgnoreCase("bye")) {
                    break;
                }

                serverMessage = input.readLine();
                System.out.println(serverMessage);
            }

            closeEverything();

        } catch (IOException e) {
            System.out.println("Error sending message.");
            e.printStackTrace();
            closeEverything();
        }
    }

    public void closeEverything() {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing client.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 5000, "Player1");
        client.sendMessage();
    }
}

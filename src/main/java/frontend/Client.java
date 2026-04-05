package frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import static java.lang.Thread.sleep;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public Client(String host, int port) {

        // 1 second delay added for compound run application
        try
        {
            sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to server.");
            GUI gui = new GUI(); // Start graphical interface
        } catch (IOException e) {
            System.out.println("Error connecting to server.");
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        if (socket == null || input == null || output == null) {
            System.out.println("Client is not connected to the server.");
            return;
        }
        try {
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            Thread readThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String serverMessage;
                    try {
                        while ((serverMessage = input.readLine()) != null) {
                            System.out.println(serverMessage);
                        }
                    } catch (IOException e) {
                        System.out.println("Disconnected from server.");
                    }
                }
            });
            readThread.start();

            String message;
            while ((message = keyboard.readLine()) != null) {
                output.println(message);

                if (message.equalsIgnoreCase("BYE")) {
                    break;
                }
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
        Client client = new Client("localhost", 5050);
        client.sendMessage();
    }
}

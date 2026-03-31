package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnectionHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;

    public ClientConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            output.println("Connected to the server.");

            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("Client says: " + message);

                if (message.equalsIgnoreCase("bye")) {
                    output.println("Goodbye from server.");
                    break;
                } else {
                    output.println("Server received: " + message);
                }
            }

            closeEverything();

        } catch (IOException e) {
            System.out.println("Error handling client connection.");
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
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing connection.");
            e.printStackTrace();
        }
    }
}

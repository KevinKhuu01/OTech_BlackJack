package frontend;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private GUI gui;

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/
    public Client(String host, int port)
    {
        try
        {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to server.");
            gui = new GUI(this); // Start graphical interface
            receiveMessage();

        }
        catch (IOException e)
        {
            System.out.println("Error connecting to server.");
            e.printStackTrace();
        }
    }

    /** CONNECTION HANDLER (RECEIVE MESSAGE FROM SERVER) --------------------------------------------------------------------------------------------------- **/
    public void receiveMessage() {
        if (socket == null || input == null || output == null) {
            System.out.println("Client is not connected to the server.");
        }

        Thread readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String serverMessage;
                try {
                    while ((serverMessage = input.readLine()) != null) {
                        System.out.println("Server: " + serverMessage);

                        final String message = serverMessage;
                        if (gui != null) {
                            SwingUtilities.invokeLater(() -> gui.processServerResponse(message));
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }
        });
        readThread.start();
    }

    /** METHODS --------------------------------------------------------------------------------------------------- **/
    public void sendMessage(String message)
    {
        output.println(message);
    }

    public void closeSockets() {
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

    public static void main(String[] args)
    {
        Client client = new Client("localhost", 5050);
    }
}

package server;

import database.InitializeDB;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    private ServerSocket serverSocket;
    private final int port;

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/
    public Server(int port) {
        this.port = port;
    }

    /** METHODS --------------------------------------------------------------------------------------------------- **/

    /** Starts up the server, sending messages to the terminal to confirm:
     * - The server has started on a given port
     * - The server is listening and is waiting for a client
     * - A client has connected
     * An error occurred while starting
     **/
    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                System.out.println("Waiting for client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("A client connected.");

                ClientConnectionHandler handler = new ClientConnectionHandler(clientSocket);
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Error starting server.");
            e.printStackTrace();
        }
    }

    /** Prints messages to the terminal indicating that the server is closed or
     * if there was an error closing the server
     **/
    public void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("Server closed.");
            }
        } catch (IOException e) {
            System.out.println("Error closing server.");
            e.printStackTrace();
        }
    }

    // Main method
    public static void main(String[] args) {
        InitializeDB.initialize();
        Server server = new Server(5050);
        server.startServer();
    }
}

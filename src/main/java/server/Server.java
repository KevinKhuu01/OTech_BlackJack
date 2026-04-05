package server;

import backend.GameLogic;
import database.InitializeDB;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private final int port;

    public static ArrayList<ClientConnectionHandler> clientHandlers = new ArrayList<ClientConnectionHandler>();

    public Server(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            new GameLogic();
            System.out.println("Server started on port " + port);

            while (true) {
                System.out.println("Waiting for a client...");

                Socket clientSocket = serverSocket.accept();
                System.out.println("A client connected.");

                ClientConnectionHandler handler = new ClientConnectionHandler(clientSocket);
                clientHandlers.add(handler);
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Error starting server.");
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message) {
        for (ClientConnectionHandler handler : clientHandlers) {
            handler.sendMessage(message);
        }
    }

    public static void removeHandler(ClientConnectionHandler handler) {
        clientHandlers.remove(handler);
    }

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

    public static void main(String[] args) {
        Server server = new Server(5050);
        server.startServer();
        InitializeDB.initialize();
    }
}

package server;

import backend.GameLogic;
import backend.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnectionHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private String playerName;

    public ClientConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            output.println("Connected to the server.");
            output.println("Commands: JOIN name, START, HIT, STAY, STATE, BYE");

            String message;

            while ((message = input.readLine()) != null) {
                System.out.println("Client says: " + message);

                if (message.startsWith("JOIN ")) {
                    playerName = message.substring(5).trim();
                    GameLogic.addPlayer(playerName);
                    Server.broadcastMessage(playerName + " joined the game.");
                }
                else if (message.equalsIgnoreCase("START")) {
                    GameLogic.startGame();
                    Server.broadcastMessage("Game started.");
                    Server.broadcastMessage(GameLogic.getGameState());
                    String result = GameLogic.getRoundResult(playerName);
                    if (!result.equals("PLAYING"))
                    {
                        output.println(result);
                    }
                }
                else if (message.equalsIgnoreCase("HIT")) {
                    if (playerName != null) {
                        Player player = GameLogic.getPlayer(playerName);
                        GameLogic.hit(player);
                        Server.broadcastMessage(playerName + " chose HIT.");
                        Server.broadcastMessage(GameLogic.getGameState());
                    } else {
                        output.println("You must JOIN first.");
                    }
                    String result = GameLogic.getRoundResult(playerName);
                    if (!result.equals("PLAYING"))
                    {
                        output.println(result);
                    }
                }
                else if (message.equalsIgnoreCase("STAY")) {
                    if (playerName != null) {
                        Player player = GameLogic.getPlayer(playerName);
                        GameLogic.stay(player);
                        Server.broadcastMessage(playerName + " chose STAY.");
                        Server.broadcastMessage(GameLogic.getGameState());
                    } else {
                        output.println("You must JOIN first.");
                    }
                    String result = GameLogic.getRoundResult(playerName);
                    if (!result.equals("PLAYING"))
                    {
                        output.println(result);
                    }
                }
                else if (message.equalsIgnoreCase("STATE")) {
                    output.println(GameLogic.getGameState());
                }
                else if (message.equalsIgnoreCase("BYE")) {
                    output.println("Goodbye from server.");
                    break;
                }
                else {
                    output.println("Unknown command.");
                }
            }

            closeEverything();

        } catch (IOException e) {
            System.out.println("Error handling client connection.");
            e.printStackTrace();
            closeEverything();
        }
    }

    public void sendMessage(String message) {
        output.println(message);
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

package game;

import client.ClientHandler;
import model.player.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class GameServer implements Runnable {

    private int serverPort;
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;
    private int tableMax;

    GameServer(int port, int tableMax) {
        serverPort = port;
        this.tableMax = tableMax;
    }

    public void run() {
        openServerSocket();
        var rules = new Rules(1.0, 2.0, 0.0, -1, tableMax, Rules.GameType.HOLDEM);
        var clientHandler = new ClientHandler();
        var game = new Game(new ArrayList<>(), rules, clientHandler);
        while(!isStopped()) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(() -> {
                try {
                    var inputStream = clientSocket.getInputStream();
                    var scanner = new Scanner(inputStream);
                    if(scanner.hasNextLine()) {
                        var line = scanner.nextLine();
                        var parts = line.split(":");
                        var name = parts[0];
                        var amount = Double.parseDouble(parts[1]);
                        var player = new Player(amount, name);
                        clientHandler.addClient(player.getPlayerId(), clientSocket);
                        game.addPlayer(player);
                        if(game.getNumPlayers() >= 2) {
                            game.runGame();
                        }
                    }
                } catch (IOException | IndexOutOfBoundsException | NumberFormatException e) {
                    e.printStackTrace();
                } catch (Game.TableFullException e) {
                    e.printStackTrace();
                    System.out.println("Table full");
                }
            }).start();
        }
        System.out.println("Server stopped.");
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }

    public synchronized void stop() {
        isStopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }

    public static void main(String[] args) {
        var port = Integer.parseInt(args[0]);
        var tableMax = Integer.parseInt(args[1]);
        var gameServer = new GameServer(port, tableMax);
        gameServer.run();
    }
}

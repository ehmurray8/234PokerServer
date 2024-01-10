package game;

import client.ClientHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameServer implements Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final int serverPort;
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;
    private final int tableMax;

    GameServer(int port, int tableMax) {
        serverPort = port;
        this.tableMax = tableMax;
    }

    public void run() {
        openServerSocket();
        var rules = new Rules(1.0, 2.0, 0.0, -1, tableMax, Rules.GameType.HOLDEM);
        var clientHandler = new ClientHandler();
        var game = new Game(new ArrayList<>(), rules, clientHandler);
        while (!isStopped()) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(() -> {
                try {
                    var inputStream = clientSocket.getInputStream();
                    var scanner = new Scanner(inputStream);
                    if (scanner.hasNextLine()) {
                        var line = scanner.nextLine();
                        var parts = line.split(":");
                        var name = parts[0];
                        var amount = Double.parseDouble(parts[1]);
                        var player = new Player(amount, name);
                        clientHandler.addClient(player.getPlayerId(), clientSocket);
                        game.addPlayer(player);
                        if (game.getNumPlayers() >= 2) {
                            game.runGame();
                        }
                    }
                } catch (Game.TableFullException e) {
                    log.warn("Table is full.", e);
                } catch (Exception e) {
                    log.error("Failed to handle client.", e);
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
        // TODO: Un-hardcode these
        var port = 8080;
        var tableMax = 6;
        var gameServer = new GameServer(8080, tableMax);
        gameServer.run();
    }
}

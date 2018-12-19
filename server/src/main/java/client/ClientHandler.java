package client;

import game.RegularUpdate;
import game.Rules;
import model.option.Option;
import model.player.Player;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class ClientHandler {

    private HashMap<UUID, Socket> players = new HashMap<>();

    public ClientHandler() { }

    public void addClient(UUID id, Socket socket) {
        players.put(id, socket);
    }

    public Rules.GameType getDesiredGameType(UUID playerId) {
        return Rules.GameType.HOLDEM;
    }

    public Option getDesiredOption(UUID playerId, List<Option> options) {
        return options.get(0);
    }

    public Option getDesiredOption(UUID playerId, HashMap<UUID, RegularUpdate> updates, List<Option> options) {
        var threads = new ArrayList<Thread>();
        var ref = new Object() {
            Option option = options.get(0);
        };
        for(var entry: players.entrySet()) {
            var id = entry.getKey();
            var socket = entry.getValue();
            var update = updates.get(id);
            var thread = new Thread(() -> {
                sendMessage(socket, update);
                if(playerId == id) {
                    ref.option = getResponse(socket, update.getActionLimit(), options);
                }
            });
            threads.add(thread);
            thread.start();
        }

        for(var thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return ref.option;
    }

    private void sendMessage(Socket socket, RegularUpdate update) {
        try {
            var outputStream = socket.getOutputStream();
            var printWriter = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
            printWriter.println(update.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Option getResponse(Socket socket, int actionLimit, List<Option> options) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(new ReceiveOptionResponse(socket, options));
        if(actionLimit != -1) {
            try {
                return future.get(actionLimit, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                future.cancel(true);
            }
            executor.shutdownNow();
        } else {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                future.cancel(true);
            }
        }
        return options.get(0);
    }

    class ReceiveOptionResponse implements Callable<Option> {

        private Socket socket;
        private List<Option> options;

        ReceiveOptionResponse(Socket socket, List<Option> options) {
            this.socket = socket;
            this.options = options;
        }

        @Override
        public Option call() {
            try {
                var inputStream = socket.getInputStream();
                var scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
                if(scanner.hasNextLine()) {
                    var line = scanner.nextLine();
                    var parts = line.split(":");
                    var index = Integer.parseInt(parts[0]);
                    var amount = Double.parseDouble(parts[1]);
                    return new Option(options.get(index).getType(), amount);
                }
            } catch (IOException | IndexOutOfBoundsException | NumberFormatException e) {
                e.printStackTrace();
            }
            return options.get(0);
        }
    }
}

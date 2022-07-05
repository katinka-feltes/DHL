package dhl.network;

import dhl.Constants;
import dhl.controller.player_logic.Human;
import dhl.model.Game;
import dhl.model.Player;
import dhl.view.Cli;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private ServerSocket serverSocket;
    private final int port = Constants.PORT_NUMBER;
    List<Player> players = new ArrayList<>();
    boolean start = false;
    private Game game;

    public GameServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port "+Constants.PORT_NUMBER);
            Thread serverThread = new Thread(() -> {
                while (!start) {
                    try {
                        Socket s = serverSocket.accept();
                        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                        System.out.println("Client " + s.getLocalAddress() + " has connected");
                        Object recieved = in.readObject();
                        if (recieved instanceof Player && players.size() < 4) {
                            System.out.println(recieved);
                            Player player = (Player) recieved;
                            System.out.println(player.getName());
                            players.add(player);
                        } else if (recieved.equals("start") && players.size() > 1) {
                            System.out.println(recieved);
                            start = true;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("starting Game...");
                game = new Game(players);
                System.out.println(game.getPlayers().size());
            });
            serverThread.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new GameServer();
        Client c = new Client();
        c.addPlayers();
        c.addPlayers();
        c.startGame();
    }
}

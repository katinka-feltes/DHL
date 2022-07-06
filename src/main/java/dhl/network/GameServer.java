package dhl.network;

import dhl.Constants;
import dhl.model.Game;
import dhl.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private ServerSocket ss;
    List<Player> players = new ArrayList<>();
    boolean start = false;
    private Game game;

    public GameServer(String ip) {
        try {
            ss = new ServerSocket();
            ss.bind(new InetSocketAddress(InetAddress.getByName(ip), Constants.PORT_NUMBER));
            System.out.println("Server is listening on " + ss.getLocalSocketAddress());
            Thread serverThread = new Thread(() -> {
                Object received = "";
                while (!received.equals("start")) {
                    try {
                        Socket s = ss.accept();
                        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                        System.out.println("Client " + s.getLocalAddress() + " has connected");
                        received= in.readObject();
                        if (received instanceof Player && players.size() < 4) {
                            Player player = (Player) received;
                            System.out.println(player.getName());
                            players.add(player);
                        } else if (received.equals("start") && players.size() > 1) {
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
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        new GameServer(InetAddress.getLocalHost().getHostAddress());
        Client c = new Client();
        c.addPlayer(InetAddress.getLocalHost().getHostAddress());
    }
}

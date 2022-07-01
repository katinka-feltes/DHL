package dhl.network;

import dhl.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket ss;
    private int numPlayers;
    public Server() {
        this.numPlayers = 0;
        try {
            ss = new ServerSocket(Constants.PORT_NUMBER);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void accept() {
        try {
            while (numPlayers < 3) { //TODO: adaptive number of players
                Socket socket = ss.accept();
                numPlayers++;
                System.out.println("Player " + numPlayers + " connected");
                ServerSideConnection ssc = new ServerSideConnection(socket, numPlayers);

            }
            System.out.println("All players connected");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private class ServerSideConnection implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private int playerID;

        public ServerSideConnection(Socket socket, int playerID) {
            this.socket = socket;
            this.playerID = playerID;
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                out.writeInt(playerID);
                out.flush();

                while (true) {
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.accept();
    }
}

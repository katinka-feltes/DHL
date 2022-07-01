package dhl.network;

import dhl.Constants;
import dhl.model.Game;

import java.io.ObjectInputStream;
import java.net.Socket;

public class Client implements Runnable {

    @Override
    public void run() {
        try (Socket socket = new Socket("localhost", Constants.PORT_NUMBER)) {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Game game = (Game) in.readObject();
            System.out.println("Received package: " + game);

            System.out.println("Connected to " + socket.getInetAddress().getHostName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

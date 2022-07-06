package dhl.network;

import dhl.Constants;
import dhl.controller.player_logic.Human;
import dhl.model.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public void addPlayers(){
        try(Socket socket = new Socket("172.20.10.12", Constants.PORT_NUMBER)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            Player player = new Player("Hat geklappt", 't', new Human());
            out.writeObject(player);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startGame(){
        try(Socket socket = new Socket("172.20.10.12", Constants.PORT_NUMBER)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject("start");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
        c.addPlayers();
        c.startGame();
    }
}

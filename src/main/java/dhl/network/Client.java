package dhl.network;

import dhl.Constants;
import dhl.controller.player_logic.Human;
import dhl.model.Player;
import dhl.view.Cli;

import java.io.*;
import java.net.Socket;

public class Client {
    public void addPlayers(){
        try(Socket socket = new Socket("localhost", Constants.PORT_NUMBER)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            Player player = new Player("Hat geklappt", 't', new Human(new Cli()));
            out.writeObject(player);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startGame(){
        try(Socket socket = new Socket("localhost", Constants.PORT_NUMBER)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject("start");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

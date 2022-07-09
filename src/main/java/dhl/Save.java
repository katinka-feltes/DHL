package dhl;

import dhl.model.Game;

import java.io.*;

/**
 * Class with the methods to save the game and load it from the saved file
 */
public class Save {

    /**
     * saves the game as a file in resorces
     * @param g the game to serialize
     * @throws IOException if a problem occurs while writing the file
     */
    public static void serializeDataOut(Game g) throws IOException {
        FileOutputStream fos = new FileOutputStream("src/main/resources/savedGame.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(g);
        oos.close();
        fos.close();
    }

    /**
     * loads the saved game from file savedGame.txt in resources
     * @return the game from the file serialized
     */
    public static Game serializeDataIn(){
        try {
            FileInputStream fin = new FileInputStream("src/main/resources/savedGame.txt");
            ObjectInputStream ois = new ObjectInputStream(fin);
            Game loadedGame = (Game) ois.readObject();
            ois.close();
            return loadedGame;
        } catch (Exception ex){
            System.out.println("Error while loading from file:" + ex.getMessage());
            return null;
        }
    }
}

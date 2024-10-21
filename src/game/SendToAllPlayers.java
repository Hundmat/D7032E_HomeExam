package game;

import java.util.ArrayList;
import game.players.*;

public class SendToAllPlayers {
    ArrayList<Player> players;
    public SendToAllPlayers(ArrayList<Player> players) {
        this.players = players;

    }

    public void sendToAllPlayers(String message){
        for (Player player : this.players) {
            if(!player.isBot()){
                player.sendMessage(message);
            }else{
                System.out.println(message);
            }
            
        }
    }
}

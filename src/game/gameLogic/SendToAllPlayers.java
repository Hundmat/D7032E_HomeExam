package game.gameLogic;

import java.util.ArrayList;
import game.players.*;

public class SendToAllPlayers {
    ArrayList<Player> players;
    public SendToAllPlayers(ArrayList<Player> players) {
        this.players = players;

    }

    /**
     * Sends a message to all players in the game.
     * 
     * @param message The message to be sent to all players.
     *                If the player is not a bot, the message is sent using the player's sendMessage method.
     *                If the player is a bot, the message is printed to the console.
     */
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

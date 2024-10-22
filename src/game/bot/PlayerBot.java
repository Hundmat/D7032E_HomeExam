
package game.bot;

import java.util.ArrayList;
import game.piles.Card;
import game.players.Player;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class PlayerBot extends Player {
    String lastMessage = "";
    public PlayerBot(int playerID, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient, boolean online){
        super(playerID, connection, inFromClient, outToClient,online);
        
        
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public String readMessage() {
        String word = ""; 
		
		try{word = (String) inFromClient.readObject();} catch (Exception e){}
        return word;
    }

    public void sendMessage(Object message) {
        System.out.println(message);
    }

    public void setScore(int score) {
		this.score = score;
   }

   public int getScore() {
	   return this.score;
   }

    
    public boolean isBot() {
        return true;  
    }

    public void update(ObjectInputStream inFromClient, ObjectOutputStream outToClient, Socket connection) {
		this.inFromClient = inFromClient;
		this.outToClient = outToClient;
		this.connection = connection;
	}
  
    public String getLastMessage() {
        return this.lastMessage;
    }

    
    
}

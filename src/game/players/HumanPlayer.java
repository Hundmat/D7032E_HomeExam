package game.players;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import game.piles.Card;

public class HumanPlayer extends Player{
	
	
	

	



	public HumanPlayer(int playerID, ObjectInputStream inFromClient, ObjectOutputStream outToClient, Socket connection, boolean online) {
		super(playerID, connection, inFromClient, outToClient, online);
		
		
	}

	

	public void addCardToHand(Card card) {
		hand.add(card);
	}

	public int getPlayerID() {
		return super.playerID;
	}

	public void update(ObjectInputStream inFromClient, ObjectOutputStream outToClient, Socket connection,boolean online) {
		super.inFromClient = inFromClient;
		super.outToClient = outToClient;
		super.connection = connection;
		super.online = online;

	}

	public void sendMessage(Object message) {
		
		try {super.outToClient.writeObject(message);} catch (Exception e) {}
        
	}

   public void setScore(int score) {
		super.score = score;
   }

   public int getScore() {
	   return super.score;
   }

	public String readMessage() {
		String word = ""; 
			
		try{word = (String) super.inFromClient.readObject();} catch (Exception e){}

		return word;
	}

	public boolean isBot() {
		return false;
	}
}
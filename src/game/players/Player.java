package game.players;

import game.piles.Card;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.ArrayList;

public abstract class Player {

    // Player attributes
    protected int playerID;
    protected boolean online;
    protected ArrayList<Card> hand = new ArrayList<>();
    protected Socket connection;
    protected ObjectInputStream inFromClient;
    protected ObjectOutputStream outToClient;
    protected int score = 0;
    protected String lastMessage = "";


    // Constructor
    public Player(int playerID, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient, boolean online) {
        this.playerID = playerID;
        this.connection = connection;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
    }

    public void sendMessage(Object message) {
		
		if(online) {
            try {outToClient.writeObject(message);} catch (Exception e) {}
            this.lastMessage = message.toString();
        }
	}

    public void setScore(int score) {
		this.score = score;
   }

   public int getScore() {
	   return this.score;
   }

	public String readMessage() {
        String word = ""; 
		
		try{word = (String) inFromClient.readObject();} catch (Exception e){}
        return word;
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

    // Method to add a card to the player's hand
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    // Method to get the player's unique ID
    public int getPlayerID() {
        return this.playerID;
    }

    // Method to get the player's current hand of cards
    public ArrayList<Card> getHand() {
        return this.hand;
    }

  

    // Abstract method to determine if the player is a bot (must be defined by subclasses)
    public abstract boolean isBot();

    public void update(ObjectInputStream inFromClient, ObjectOutputStream outToClient, Socket connection,boolean online) {
		this.inFromClient = inFromClient;
		this.outToClient = outToClient;
		this.connection = connection;
        this.online = online;
	}
}

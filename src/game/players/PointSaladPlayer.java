package game.players;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import game.piles.Card;

public class PointSaladPlayer<T extends Enum<T>> extends Player<T> {
	public boolean online;
	public Socket connection;
	public ObjectInputStream inFromClient;
	public ObjectOutputStream outToClient;
	public ArrayList<String> region = new ArrayList<String>();
	Scanner in = new Scanner(System.in);
	public ArrayList<Card<T>> hand = new ArrayList<Card<T>>();
	public int score = 0;
	



	public PointSaladPlayer(int playerID, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient, long seed) {
		super(playerID, seed);
		this.connection = connection; 
		this.inFromClient = inFromClient; 
		this.outToClient = outToClient; 
		if(connection == null)
			this.online = false;
		else
			this.online = true;
		this.seed = seed;
	}
	public void addCardToHand(Card<T> card) {
		hand.add(card);
	}

	public int getPlayerID() {
		return this.playerID;
	}

	public ArrayList<Card<T>> getHand() {
		return this.hand;
	}

	public void sendMessage(Object message) {
		if (!online) {
			System.out.println(message);
		}
		else {
			try {
				outToClient.writeObject(message);
			} catch (Exception e) {}
		}            
		return;
	}
	
	public long getSeed() {
		return this.seed;
	}

	@Override
	public String readMessage() {
		String word = ""; 
		if(online)
			try{word = (String) inFromClient.readObject();} catch (Exception e){}
		else
			try {word=in.nextLine();} catch(Exception e){}
		return word;
	}

	public boolean isBot() {
		return false;
	}
}
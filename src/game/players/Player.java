package game.players;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.smartcardio.Card;

public class player {
    public int playerID;
		public boolean online;
		public boolean isBot;
		public Socket connection;
		public ObjectInputStream inFromClient;
		public ObjectOutputStream outToClient;
		public ArrayList<String> region = new ArrayList<String>();
		Scanner in = new Scanner(System.in);
		public ArrayList<Card> hand = new ArrayList<Card>();
		public int score = 0;



		public Player(int playerID, boolean isBot, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient) {
			this.playerID = playerID; this.connection = connection; this.inFromClient = inFromClient; this.outToClient = outToClient; this.isBot = isBot;
			if(connection == null)
				this.online = false;
			else
				this.online = true;
		}
		
		
}

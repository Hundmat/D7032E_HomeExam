
package game.bot;

import java.util.ArrayList;
import game.piles.Card;
import game.players.Player;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class PlayerBot extends Player {
    public ArrayList<Card> hand;
   
    public PlayerBot(int playerID, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient, boolean online){
        super(playerID, connection, inFromClient, outToClient,online);
        
        this.hand = new ArrayList<Card>();
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }

    
    public boolean isBot() {
        return true;  
    }

    public void update(ObjectInputStream inFromClient, ObjectOutputStream outToClient, Socket connection) {
		this.inFromClient = inFromClient;
		this.outToClient = outToClient;
		this.connection = connection;
	}
  
    
    
}

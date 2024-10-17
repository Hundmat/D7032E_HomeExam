
package game.players;

import java.util.ArrayList;
import game.piles.Card;


public class PlayerBot<T extends Enum<T>> extends Player<T> {
    public ArrayList<Card<T>> hand;
    private String nextChoise = null;
    private String message;

    public PlayerBot(int playerID,long seed){
        super(playerID,seed);
        this.playerID = playerID;
        this.hand = new ArrayList<Card<T>>();
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public void addCardToHand(Card<T> card) {
        hand.add(card);
    }

    public ArrayList<Card<T>> getHand() {
        return this.hand;
    }

    
    public boolean isBot() {
        return true;  
    }

    @Override
    public void sendMessage(Object message) {
        System.out.println(message);
        this.message = (String) message;
    }

    public long getSeed() {
		return this.seed;
	}

    @Override
    public String readMessage() {
        return this.message;
    }
    
}

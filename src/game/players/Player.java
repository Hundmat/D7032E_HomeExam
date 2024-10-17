package game.players;

import game.piles.Card;

import java.util.ArrayList;

public abstract class Player<T extends Enum<T>> {

    // Player attributes
    protected int playerID;
    protected boolean online;
    protected ArrayList<Card<T>> hand = new ArrayList<>();
    protected long seed;

    // Constructor
    public Player(int playerID, long seed) {
        this.playerID = playerID;
        this.seed = seed;
    }

    // Method to add a card to the player's hand
    public void addCardToHand(Card<T> card) {
        hand.add(card);
    }

    // Method to get the player's unique ID
    public int getPlayerID() {
        return this.playerID;
    }

    // Method to get the player's current hand of cards
    public ArrayList<Card<T>> getHand() {
        return this.hand;
    }

    // Abstract method to send a message (must be defined by subclasses)
    public abstract void sendMessage(Object message);

    // Abstract method to read a message (must be defined by subclasses)
    public abstract String readMessage();

    // Abstract method to determine if the player is a bot (must be defined by subclasses)
    public abstract boolean isBot();

    // Method to get the player's seed (randomization)
    public long getSeed() {
        return this.seed;
    }
}

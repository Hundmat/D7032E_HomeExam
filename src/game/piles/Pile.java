package game.piles;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a pile of cards.
 */
public class Pile {

    public ArrayList<Card> cards = new ArrayList<Card>();
    

    public Pile() {
        
        
        
    }

    /**
     * Adds a card to the pile.
     *
     * @param card the card to be added
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Retrieves all cards in the pile.
     *
     * @return an ArrayList of all cards in the pile
     */
    public ArrayList<Card> getAll() {
        return cards;
    }

    /**
     * Removes and returns the card at the specified index.
     *
     * @param i the index of the card to be removed
     * @return the card at the specified index, or null if the pile is empty
     */
    public Card buyCard(int i) {
        if(cards.size() > 0) {
            return cards.remove(i);
        } else {
            return null;
        }
    }

    /**
     * Retrieves the card at the specified index without removing it.
     *
     * @param i the index of the card to be retrieved
     * @return the card at the specified index, or null if the pile is empty
     */
    public Card getCard(int i) {
        if(cards.size() > 0) {
            return cards.get(i);
        } else {
            return null;
        }
    }

    /**
     * Removes and returns the last card in the pile, flipping its side.
     *
     * @return the last card in the pile, or null if the pile is empty
     */
    public Card buyLastCard() {
        if(cards.size() > 0) {
            cards.get(cards.size() - 1).flipSide();
            return cards.remove(cards.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * Removes and returns the first card in the pile, flipping its side.
     *
     * @return the first card in the pile, or null if the pile is empty
     */
    public Card toMarket() {
        if(cards.size() > 0) {
            cards.get(0).flipSide();
            return cards.remove(0);
        } else {
            return null;
        }
    }

    /**
     * Retrieves the number of cards in the pile.
     *
     * @return the number of cards in the pile
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Shuffles the cards in the pile.
     */
    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    /**
     * Checks if the pile is empty.
     *
     * @return true if the pile is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Replaces the card at the specified index with a new card.
     *
     * @param i the index of the card to be replaced
     * @param card the new card to set at the specified index
     */
    public void set(int i, Card card) {
        cards.set(i, card);
    }
}

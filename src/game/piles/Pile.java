package game.piles;

import java.util.ArrayList;
import java.util.Collections;

public class Pile <T extends Enum<T>> {
    public ArrayList<Card<T>> cards = new ArrayList<Card<T>>();
    int playingField;
    public ArrayList<Card<T>> fieldCards;
    long seed;
    public Pile(int playingField, long seed) {
        this.playingField = playingField;
        this.seed = seed;


        this.fieldCards = new ArrayList<Card<T>>();
        this.fieldCards.ensureCapacity(playingField);

        
        try {
            setFieldCards();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error setting field cards, no cards in pile");
        }

    }

    public void addCard(Card<T> card) {
        cards.add(card);
    }

    public Card<T> buyCard() {
        if(cards.size() > 0) {
            return cards.remove(0);
        } else {
            return null;
        }
    }

    public Card<T> getCard() {
        if(cards.size() > 0) {
            return cards.get(0);
        } else {
            return null;
        }
    }

    public void flipSide() {
        if(cards.size() > 0) {
            cards.get(0).flipSide();
        }
    }

    public void shuffleDeck() {
		Collections.shuffle(cards);
	}

    private void setFieldCards() {
        for(int i = 0; i < playingField; i++) {
            this.fieldCards.add(cards.remove(0));
            this.fieldCards.get(0).criteriaSideUp = false;
        }
    }
}
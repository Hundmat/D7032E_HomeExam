package game.piles;

import java.util.ArrayList;
import java.util.Collections;

public class Pile{
   
    
    public ArrayList<Card> cards = new ArrayList<Card>();
    

    public Pile() {
        
        
        
    }

    public void addCard(Card card) {
        cards.add(card);
    }
    
    public ArrayList<Card> getAll() {
        return cards;
    }

    public Card buyCard(int i) {
        if(cards.size() > 0) {
            return cards.remove(i);
        } else {
            return null;
        }
    }

    public Card getCard(int i) {
        if(cards.size() > 0) {
            return cards.get(i);
        } else {
            return null;
        }
    }

    public Card buyLastCard() {
        if(cards.size() > 0) {
            cards.get(cards.size() - 1).flipSide();
            return cards.remove(cards.size() - 1);
        } else {
            return null;
        }
    }

    public Card toMarket() {
        if(cards.size() > 0) {
            
            cards.get(0).flipSide();
            return cards.remove(0);
            
        }
        else {
            return null;
        }
    }

    public int getSize() {
        return cards.size();
    }

    public void shuffleDeck() {
		Collections.shuffle(cards);
	}
    
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void set(int i, Card card) {
        cards.set(i, card);
    }
    
    
}
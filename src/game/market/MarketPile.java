package game.market;

import java.util.ArrayList;

import game.piles.Card;
import game.piles.Pile;
import game.piles.PileHandler;

public class MarketPile extends PileHandler {
    private ArrayList<Pile> marketPile = new ArrayList<>();

    public MarketPile(int piles) {
        super(piles);
        for (int i = 0; i < piles; i++) {
            this.marketPile.add(new Pile());
        }

        for (int i = 0; i < marketPile.size(); i++) {
            this.marketPile.get(i).addCard(null);
            this.marketPile.get(i).addCard(null);
        }
    }

    public void addCardToMarket(Card card, int pile) {
        for (int i = 0; i < marketPile.get(pile).getSize(); i++) {
            if(marketPile.get(pile).getCard(i) == null) {
                marketPile.get(pile).set(i, card);
                return;
            }
        }
        
    }

    public Card getCardFromMarket(int pile, int cardIndex) {
        return this.marketPile.get(pile).getCard(cardIndex);
    }

    public Card buyCard(int pile, int cardIndex) {
        
    
        // Check if the pile has any cards
        if (this.marketPile.get(pile).isEmpty()) {
            return null;  // Return null if the pile is empty
        }
        Card returncard = this.marketPile.get(pile).getCard(cardIndex);
        // Try to buy the card if the card index is valid
        this.marketPile.get(pile).set(cardIndex, null);
        return returncard;
    }
    

    public int size() {
        return this.marketPile.size();
    }

    public Pile getPile(int i) {
        return this.marketPile.get(i);
    }

    public ArrayList<Pile> getPiles() {
        return this.marketPile;
    }
    
    public boolean isEmpty() {
        return this.marketPile.isEmpty();
    }
    
    

    


    
    
}

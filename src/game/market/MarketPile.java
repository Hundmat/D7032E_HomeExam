package game.market;

import java.util.ArrayList;

import game.piles.Card;
import game.piles.Pile;
import game.piles.PileHandler;

public class MarketPile extends PileHandler {
    private ArrayList<Pile> marketPile = new ArrayList<>();

    /**
     * Constructs a MarketPile with the specified number of piles.
     * Each pile is initialized with two null cards.
     *
     * @param piles the number of piles to create
     */
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

    /**
     * Adds a card to the specified pile in the market.
     * If there is an empty slot (null) in the pile, the card is added to that slot.
     *
     * @param card the card to add
     * @param pile the index of the pile to add the card to
     */
    public void addCardToMarket(Card card, int pile) {
        for (int i = 0; i < marketPile.get(pile).getSize(); i++) {
            if(marketPile.get(pile).getCard(i) == null) {
                marketPile.get(pile).set(i, card);
                return;
            }
        }
    }

    /**
     * Retrieves a card from the specified pile and card index in the market.
     *
     * @param pile the index of the pile
     * @param cardIndex the index of the card in the pile
     * @return the card at the specified pile and card index
     */
    public Card getCardFromMarket(int pile, int cardIndex) {
        return this.marketPile.get(pile).getCard(cardIndex);
    }

    /**
     * Buys a card from the specified pile and card index in the market.
     * The card is removed from the pile and returned.
     *
     * @param pile the index of the pile
     * @param cardIndex the index of the card in the pile
     * @return the card that was bought, or null if the pile is empty
     */
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

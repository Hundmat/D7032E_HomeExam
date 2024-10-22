package game.market;

import java.util.ArrayList;

import game.piles.Pile;

public class MarketPrinterSalad extends MarketPrinter {
    private MarketPile marketPile;
    
    public MarketPrinterSalad(MarketPile marketPile) {
        super(marketPile);
        this.marketPile = marketPile;
        
    }

    /**
     * Generates a string representation of the market, including point cards and veggie cards.
     *
     * @param piles An ArrayList of Pile objects representing the point card piles.
     * @return A formatted string representing the current state of the market.
     *
     
     */
    public String printMarket( ArrayList<Pile> piles) {
        
        StringBuilder pileString = new StringBuilder("Point Cards:\t");
    
        for (int p = 0; p < piles.size(); p++) {
            if (piles.get(p).isEmpty()) {
                pileString.append("[").append(p).append("]").append(String.format("%-43s", "Empty")).append("\t");
            } else {
                pileString.append("[").append(p).append("]").append(String.format("%-43s", piles.get(p).getCard(0))).append(piles.get(p).getSize()).append("\t");
            }
        }
    
        pileString.append("\nVeggie Cards:\t");
        char veggieCardIndex = 'A';
    
        for (int p = 0; p < marketPile.size(); p++) {
            if (this.marketPile.getPile(p).isEmpty()) {
                pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", "Empty")).append("\t");
            } else {
                pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", marketPile.getPile(p).getCard(0))).append("\t");
            }
            veggieCardIndex++;
        }
    
        pileString.append("\n\t\t");
    
        for (int p = 0; p < this.marketPile.size(); p++) {
            if (this.marketPile.getPile(p).isEmpty()) {
                pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", "Empty")).append("\t");
            } else if (marketPile.getPile(p).getSize() > 1) {
                pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", marketPile.getPile(p).getCard(1))).append("\t");
            } else {
                pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", "Empty")).append("\t");
            }
            veggieCardIndex++;
        }
    
        return pileString.toString();
    }
}

package game.market;

import java.util.ArrayList;

import game.piles.Pile;

public class MarketPrinter {
    private MarketPile marketPile;
    
    public MarketPrinter(MarketPile marketPile) {
        this.marketPile = marketPile;
        
    }

    public String printMarket( ArrayList<Pile> piles) {
        // Print debug information about marketPile size
    
        StringBuilder pileString = new StringBuilder("Point Cards:\t");
    
        // Iterate over the piles to print point cards
        for (int p = 0; p < piles.size(); p++) {
            if (piles.get(p).isEmpty()) {
                pileString.append("[").append(p).append("]").append(String.format("%-43s", "Empty")).append("\t");
            } else {
                pileString.append("[").append(p).append("]").append(String.format("%-43s", piles.get(p).getCard(0))).append(piles.get(p).getSize()).append("\t");
            }
        }
    
        pileString.append("\nVeggie Cards:\t");
        char veggieCardIndex = 'A';
    
        // Iterate over the marketPile for veggie cards (first row)
        for (int p = 0; p < marketPile.size(); p++) {
            if (this.marketPile.getPile(p).isEmpty()) {
                pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", "Empty")).append("\t");
            } else {
                pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", marketPile.getPile(p).getCard(0))).append("\t");
            }
            veggieCardIndex++;
        }
    
        pileString.append("\n\t\t");
    
        // Iterate again for the second row of veggie cards, if applicable
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

package game.market;

import game.piles.Pile;
import game.piles.PileHandler;

import java.util.ArrayList;


public abstract class MarketPrinter {
    PileHandler marketPile;
    public MarketPrinter(PileHandler marketPile) {
        this.marketPile = marketPile;
        
    }

    public String printMarket( ArrayList<Pile> piles) {
        return "";
    };
    
}

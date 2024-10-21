package game.market;

import game.piles.PileHandler;

public abstract class RefileMarket {

    PileHandler market;
    PileHandler piles;
    private int pilesEmpty = 100;
    public RefileMarket(PileHandler market, PileHandler piles){
        this.market = market;
        this.piles = piles;
    };
    
    public void run(){
        largestPile();
    };
    private int largestPile(){
        return 0;
    };
    
    public int PilesAreEmpty(){
        return this.pilesEmpty;
    }
}

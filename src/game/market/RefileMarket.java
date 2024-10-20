package game.market;

import game.piles.PileHandler;

abstract class RefileMarket {

    PileHandler market;
    PileHandler piles;
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
    
}

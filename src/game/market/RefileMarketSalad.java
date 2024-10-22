package game.market;

import game.piles.SetPileSalad;

public class RefileMarketSalad extends RefileMarket {
    private MarketPile market;
    private SetPileSalad piles;
    private int pilesEmpty = 100;

   
    private int largestPile() {
        int largest = 0;
        // Assuming `piles.getPiles()` returns a list or collection of piles
        for (int i = 0; i < this.piles.getPiles().size(); i++) {
            if (this.piles.getPile(i).getSize() > this.piles.getPile(largest).getSize()) {
                largest = i;
            }
        }

        
        return largest;
    }
    
   
    public RefileMarketSalad(MarketPile market, SetPileSalad piles) {
        super(market, piles);
        this.market = market;
        this.piles = piles;
    }

    /**
     * Executes the process of refilling the market with cards from the piles.
     * 
     */
    public void run() {
        int largestPileIndex = largestPile(); // Get the largest pile index
        if(this.market.getPile(largestPileIndex).getSize()==0){
            this.pilesEmpty=0;
        }else{
            // Assuming market and piles have the same number of piles
            for (int i = 0; i < this.market.getPiles().size(); i++) {
                // Go through each pile in the market
                for (int j = 0; j < this.market.getPile(i).getSize(); j++) {
                    // If the current card in the market is null or the corresponding pile is empty
                    if (this.market.getPile(i).getCard(j) == null) {
                        // Check if the pile at index i has cards to give
                        if (!this.piles.getPile(i).isEmpty()) {
                            // Transfer a card from pile i to the market
                            this.market.addCardToMarket(piles.getPile(i).toMarket(), i);
                        } else {
                            // If pile i is empty, take a card from the largest pile
                            if (!this.piles.getPile(largestPileIndex).isEmpty()) {
                                this.market.addCardToMarket(this.piles.getPile(largestPileIndex).buyLastCard(), i);
                            }
                        }
                    }
                }
            }
        }
        
    }

    public int PilesAreEmpty(){
        return this.pilesEmpty;
    }
}

package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


import game.market.MarketPile;
import game.market.RefileMarketSalad;
import game.piles.SetPileSalad;

import game.piles.Card;






public class GameLoopTesting {

    

    
    @Test
    public void testMarketRefillFromPointCardPile() {
        SetPileSalad setPileSalad = new SetPileSalad(2); // Using 4 players as an example
        MarketPile marketPile = new MarketPile(setPileSalad.getPiles().size());

        RefileMarketSalad refiller = new RefileMarketSalad(marketPile, setPileSalad);
        refiller.run();

        //check top card in pile
        Card card =setPileSalad.getPile(0).getCard(0);
        Enum<?> type = card.getCardType();

        //buy card
        marketPile.buyCard(0, 0);

        //refill market
        refiller.run();
       
        //check top card in pile
        assertEquals(type, marketPile.getPile(0).getCard(0).getCardType(), "The market pile should have 6 cards after buying a card.");
    }

    @Test
    public void testDrawFromBottomOfLargestPileWhenDrawPileEmpty() {
        SetPileSalad setPileSalad = new SetPileSalad(2); // Using 4 players as an example
        MarketPile marketPile = new MarketPile(setPileSalad.getPiles().size());
        RefileMarketSalad refiller = new RefileMarketSalad(marketPile, setPileSalad);

        // Empty one of the piles
        while (!setPileSalad.getPile(0).isEmpty()) {
            marketPile.buyCard(0, 0);

            //refill market
            refiller.run();
        }
        System.out.println(setPileSalad.getPile(0).isEmpty());
        System.out.println(marketPile.getPile(0).getCard(0));

        // Draw from pile 2 so it is smaller than pile 3
        marketPile.buyCard(1, 0);

        //refill market
        refiller.run();

        // Ensure the pile is empty
        assertTrue(setPileSalad.getPile(0).isEmpty(), "The pile should be empty.");


        //check bottom card in pile 3
        Card card =setPileSalad.getPile(2).getCard(setPileSalad.getPile(2).getSize()-1);
        Enum<?> type = card.getCardType();

        //buy card from A / pile 1 which is empty
        marketPile.buyCard(0, 0);
        refiller.run();

        // Check if the drawn card is from the bottom of the largest pile
        assertEquals(type, marketPile.getPile(0).getCard(0).getCardType(), "The market pile should draw last card of pile 3.");
    }
    
    
}
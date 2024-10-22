package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import game.bot.PlayerBot;
import game.gameLogic.GameLoopSalad;
import game.gameLogic.PointSalad;
import game.market.MarketPile;
import game.market.MarketPrinterSalad;
import game.market.RefileMarketSalad;
import game.piles.SetPileSalad;
import game.players.Player;
import game.players.PlayerHand;
import game.piles.Card;
import game.piles.CardSalad;
import game.piles.CardSalad.Vegetable;
import game.piles.Pile;






public class GameLoopTesting {
    private GameLoopSalad gameLoop;
    private PointSalad game;
    
    
    private ArrayList<Player> players;
    private SetPileSalad piles;
    

    
    
    @Test// Draw card from top of the pile and add it to the market pile : Rule 10
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

    @Test// Draw card from bottom of the largest pile and add it to the market pile : Rule 11
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
    
    @Test// Run 4 bots and control that the one with the higest score is announced as the winner
    public void testAnnounceWinnerWithHighestScore() {
        
        players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new PlayerBot(i,null, null, null,false));
        }
        game = new PointSalad(players);

        game.run();

        int maxScore = 0;
        int winnerID = -1;
        for (Player player : players) {
            if (player.getScore() > maxScore) {
                maxScore = player.getScore();
                winnerID = player.getPlayerID();
            }
        }

        for (Player player : players) {
            if (player.getPlayerID() == winnerID) {
                assertTrue(player.getLastMessage().contains("Congratulations! You are the winner with a score of " + maxScore));
            } else {
                assertTrue(player.getLastMessage().contains("The winner is player " + winnerID + " with a score of " + maxScore));
            }
        }
    }

    @Test
    public void testFlipPointCardToVegetableSide() {
        // Create a point card with criteria side up
        CardSalad card = new CardSalad(Vegetable.CARROT, "Criteria");
        

        // Ensure the card is on criteria side
        assertTrue(card.criteriaSideUp(), "The card should be on criteria side.");

        // Flip the card to vegetable side
        card.flipSide();

        // Ensure the card is now on vegetable side
        assertFalse(card.criteriaSideUp(), "The card should be on vegetable side.");
    }

    @Test
    public void testCannotFlipVegetableSideToPointSide() {
        // Create a point card with criteria side up
        CardSalad card = new CardSalad(Vegetable.CARROT, "Criteria");

        // Ensure the card is on criteria side
        assertTrue(card.criteriaSideUp(), "The card should be on criteria side.");

        // Flip the card to vegetable side
        card.flipSide();

        // Ensure the card is now on vegetable side
        assertFalse(card.criteriaSideUp(), "The card should be on vegetable side.");

        // Attempt to flip back to criteria side and expect an exception
        try {
            card.flipSide();
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Cannot flip card from type side to criteria side"), "Expected exception message not found.");
        }
    }

    @Test// Run 4 bots and control that all piles and market is empty when game is done
    public void testEmptyPilesAndMarketAtEnd() {
        
        players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new PlayerBot(i,null, null, null,false));
        }
        piles = new SetPileSalad(players.size()); // Using 4 players as an example
        MarketPile marketPile = new MarketPile(piles.getPiles().size());
        PlayerHand playerHand = new PlayerHand(players.size());
        MarketPrinterSalad marketPrinter = new MarketPrinterSalad(marketPile);
        RefileMarketSalad refiller = new RefileMarketSalad(marketPile, piles);
        gameLoop = new GameLoopSalad(players, piles, playerHand, marketPile, marketPrinter, refiller);
        gameLoop.runLoop();
        for(Pile pile: piles.getPiles()) {
            assertTrue(pile.isEmpty(), "All piles should be empty at the start of the game.");
        }

        for(Pile pile: marketPile.getPiles()) {
            assertTrue(pile.getCard(0)==null, "All market piles should be empty at the start of the game.");
            assertTrue(pile.getCard(1)==null, "All market piles should be empty at the start of the game.");

        }


        
    }
}
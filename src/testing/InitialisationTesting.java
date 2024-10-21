package testing;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import game.market.MarketPile;
import game.market.RefileMarketSalad;
import game.piles.Pile;
import game.piles.SetPileSalad;


public class InitialisationTesting {

    @Test
    public void testDeckComposition() throws IOException, ParseException {
        String jsonString = new String(Files.readAllBytes(Paths.get("POINTSALADMANIFEST.JSON")));
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray cards = (JSONArray) jsonObject.get("cards");

        int pepperCount = 0;
        int lettuceCount = 0;
        int carrotCount = 0;
        int cabbageCount = 0;
        int onionCount = 0;
        int tomatoCount = 0;

        for (Object cardObj : cards) {
            JSONObject card = (JSONObject) cardObj;
            JSONObject criteria = (JSONObject) card.get("criteria");

            for (Object key : criteria.keySet()) {
                String vegetable = (String) key;
                switch (vegetable) {
                    case "PEPPER":
                        pepperCount++;
                        break;
                    case "LETTUCE":
                        lettuceCount++;
                        break;
                    case "CARROT":
                        carrotCount++;
                        break;
                    case "CABBAGE":
                        cabbageCount++;
                        break;
                    case "ONION":
                        onionCount++;
                        break;
                    case "TOMATO":
                        tomatoCount++;
                        break;
                }
            }
        }

        assertEquals(18, pepperCount, "There should be 18 Pepper cards.");
        assertEquals(18, lettuceCount, "There should be 18 Lettuce cards.");
        assertEquals(18, carrotCount, "There should be 18 Carrot cards.");
        assertEquals(18, cabbageCount, "There should be 18 Cabbage cards.");
        assertEquals(18, onionCount, "There should be 18 Onion cards.");
        assertEquals(18, tomatoCount, "There should be 18 Tomato cards.");
    }

    @Test
    public void testDeckForTwoPlayers() {
        SetPileSalad setPileSalad = new SetPileSalad(2);
        int combinedDeck = 0;
        for (Pile pile : setPileSalad.getPiles()) {
            combinedDeck += pile.getSize();
        }

        assertEquals(36, combinedDeck, "The deck should consist of 36 cards for 2 players.");
    }

    @Test
    public void testDeckForThreePlayers() {
        SetPileSalad setPileSalad = new SetPileSalad(3);
        int combinedDeck = 0;
        for (Pile pile : setPileSalad.getPiles()) {
            combinedDeck += pile.getSize();
        }
        assertEquals(54, combinedDeck, "The deck should consist of 54 cards for 3 players.");
    }

    @Test
    public void testDeckForFourPlayers() {
        SetPileSalad setPileSalad = new SetPileSalad(4);
        int combinedDeck = 0;
        for (Pile pile : setPileSalad.getPiles()) {
            combinedDeck += pile.getSize();
        }
        assertEquals(72, combinedDeck, "The deck should consist of 72 cards for 4 players.");
    }

    @Test
    public void testDeckForFivePlayers() {
        SetPileSalad setPileSalad = new SetPileSalad(5);
        int combinedDeck = 0;
        for (Pile pile : setPileSalad.getPiles()) {
            combinedDeck += pile.getSize();
        }
        assertEquals(90, combinedDeck, "The deck should consist of 90 cards for 5 players.");
    }

    @Test
    public void testDeckForSixPlayers() {
        SetPileSalad setPileSalad = new SetPileSalad(6);
        int combinedDeck = 0;
        for (Pile pile : setPileSalad.getPiles()) {
            combinedDeck += pile.getSize();
        }
        assertEquals(108, combinedDeck, "The deck should consist of 108 cards for 6 players.");
    }

    @Test
    public void testInvalidNumberOfPlayers() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SetPileSalad(7);
        }, "An exception should be thrown for invalid number of players.");
    }
    
    @Test
    public void testShuffleAndCreateDrawPiles() {
        SetPileSalad setPileSalad = new SetPileSalad(4); // Using 4 players as an example
        Pile pile1 = setPileSalad.getPile(0);
        Pile pile2 = setPileSalad.getPile(1);
        Pile pile3 = setPileSalad.getPile(2);

        int totalCards = pile1.getSize() + pile2.getSize() + pile3.getSize();
        assertEquals(72, totalCards, "The total number of cards should be 72 for 4 players.");

        int maxPileSize = Math.max(pile1.getSize(), Math.max(pile2.getSize(), pile3.getSize()));
        int minPileSize = Math.min(pile1.getSize(), Math.min(pile2.getSize(), pile3.getSize()));

        assertTrue(maxPileSize - minPileSize <= 1, "The piles should be roughly equal in size.");
    }

    @Test
    public void testFlipOverTwoCardsFromEachPile() {
        SetPileSalad setPileSalad = new SetPileSalad(2); // Using 4 players as an example
        MarketPile marketPile = new MarketPile(setPileSalad.getPiles().size());

        RefileMarketSalad refiller = new RefileMarketSalad(marketPile, setPileSalad);

        refiller.run();

        // Check that the market pile has 6 cards
        assertEquals(2, marketPile.getPile(0).getSize(), "The market pile should have 2 cards.");
        assertEquals(2, marketPile.getPile(1).getSize(), "The market pile should have 2 cards.");
        assertEquals(2, marketPile.getPile(2).getSize(), "The market pile should have 2 cards.");

        // Check that each original pile has 2 cards less
        assertEquals(10, setPileSalad.getPile(0).getSize(), "Pile 1 should have 22 cards left.");
        assertEquals(10, setPileSalad.getPile(1).getSize(), "Pile 2 should have 22 cards left.");
        assertEquals(10, setPileSalad.getPile(2).getSize(), "Pile 3 should have 22 cards left.");

        // Check if write side is up in market
        assertFalse( "The first card in pile 1 should be false.",marketPile.getCardFromMarket(0, 0).isCriteriaSideUp());
        assertFalse( "The first card in pile 1 should be false.",marketPile.getCardFromMarket(0, 1).isCriteriaSideUp());

        assertFalse( "The first card in pile 2 should be false.",marketPile.getCardFromMarket(1, 0).isCriteriaSideUp());
        assertFalse( "The first card in pile 2 should be false.",marketPile.getCardFromMarket(1, 1).isCriteriaSideUp());

        assertFalse( "The first card in pile 3 should be false.",marketPile.getCardFromMarket(2, 0).isCriteriaSideUp());
        assertFalse( "The first card in pile 3 should be false.",marketPile.getCardFromMarket(2, 1).isCriteriaSideUp());

    }


}
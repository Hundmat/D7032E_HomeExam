package testing;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import game.gameLogic.PointSalad;
import game.market.MarketPile;
import game.market.RefileMarketSalad;
import game.piles.Pile;
import game.piles.SetPileSalad;
import game.players.HumanPlayer;
import game.players.Player;


public class InitialisationTesting {

    @Test // Test less than two players : rule 1
    public void testLessThanTwoPlayers() {

        ArrayList<Player> players = new ArrayList<>();
        
        players.add(new HumanPlayer(0,null, null, null,false));
        
        //one player
        assertThrows(IllegalArgumentException.class, () -> {
            new PointSalad(players);
        }, "There must be at least 2 players to play Point Salad.");

        //no player
        assertThrows(NullPointerException.class, () -> {
            new PointSalad(null);
        }, "There must be at least 2 players to play Point Salad.");
    }

    @Test // Test More than 6 players : rule 1
    public void testMoreThanSixPlayers() {

        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            players.add(new HumanPlayer(i,null, null, null,false));
        }
        assertThrows(IllegalArgumentException.class, () -> {
            new PointSalad(players);
        }, "There must be at least 2 players to play Point Salad.");


        for (int i = 0; i < 20; i++) {
            players.add(new HumanPlayer(i,null, null, null,false));
        }
        assertThrows(IllegalArgumentException.class, () -> {
            new PointSalad(players);
        }, "There must be at least 2 players to play Point Salad.");
        
        
        for (int i = 0; i < 30; i++) {
            players.add(new HumanPlayer(i,null, null, null,false));
        }
        assertThrows(IllegalArgumentException.class, () -> {
            new PointSalad(players);
        }, "There must be at least 2 players to play Point Salad.");
        
    }

    @Test // Test if the deck is composed of the 108 numbers of cards : rule 2
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

    @Test// Test if the deck is composed of the correct number of cards for 2 palyers : rule 3
    public void testDeckForTwoPlayers() {
        SetPileSalad setPileSalad = new SetPileSalad(2);
        int combinedDeck = 0;
        for (Pile pile : setPileSalad.getPiles()) {
            combinedDeck += pile.getSize();
        }

        assertEquals(36, combinedDeck, "The deck should consist of 36 cards for 2 players.");
    }

    @Test// Test if the deck is composed of the correct number of cards for 3 palyers : rule 3
    public void testDeckForThreePlayers() {
        SetPileSalad setPileSalad = new SetPileSalad(3);
        int combinedDeck = 0;
        for (Pile pile : setPileSalad.getPiles()) {
            combinedDeck += pile.getSize();
        }
        assertEquals(54, combinedDeck, "The deck should consist of 54 cards for 3 players.");
    }

    @Test// Test if the deck is composed of the correct number of cards for 4 palyers : rule 3
    public void testDeckForFourPlayers() {
        SetPileSalad setPileSalad = new SetPileSalad(4);
        int combinedDeck = 0;
        for (Pile pile : setPileSalad.getPiles()) {
            combinedDeck += pile.getSize();
        }
        assertEquals(72, combinedDeck, "The deck should consist of 72 cards for 4 players.");
    }

    @Test// Test if the deck is composed of the correct number of cards for 5 palyers : rule 3
    public void testDeckForFivePlayers() {
        SetPileSalad setPileSalad = new SetPileSalad(5);
        int combinedDeck = 0;
        for (Pile pile : setPileSalad.getPiles()) {
            combinedDeck += pile.getSize();
        }
        assertEquals(90, combinedDeck, "The deck should consist of 90 cards for 5 players.");
    }

    @Test// Test if the deck is composed of the correct number of cards for 6 palyers : rule 3
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
    
    @Test// Test if the deck is shuffled and the draw piles are created roughly the same size : rule 4
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

    @Test// Test if the market is created from the piles : rule 5
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
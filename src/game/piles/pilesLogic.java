package game.piles;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


import org.json.JSONArray;
import org.json.JSONObject;

public class pilesLogic {
    ArrayList<card> deckPepper = new ArrayList<>();
    ArrayList<card> deckLettuce = new ArrayList<>();
    ArrayList<card> deckCarrot = new ArrayList<>();
    ArrayList<card> deckCabbage = new ArrayList<>();
    ArrayList<card> deckOnion = new ArrayList<>();
    ArrayList<card> deckTomato = new ArrayList<>();
    ArrayList<card> deck = new ArrayList<>();
    ArrayList<pile> piles = new ArrayList<>();
    int nrPlayers;
    int biggestPileIndex = 0;
    int biggestSize = 0;

    public pilesLogic(int nrPlayers) {
        // TODO Auto-generated constructor stub
        this.nrPlayers = nrPlayers;
        setPiles();
    }

    private void setPiles() {
        

        try (InputStream fInputStream = new FileInputStream("PointSaladManifest.json");
             Scanner scanner = new Scanner(fInputStream, "UTF-8").useDelimiter("\\A")) {

            // Read the entire JSON file into a String
            String jsonString = scanner.hasNext() ? scanner.next() : "";

            // Parse the JSON string into a JSONObject
            JSONObject jsonObject = new JSONObject(jsonString);

            // Get the "cards" array from the JSONObject
            JSONArray cardsArray = jsonObject.getJSONArray("cards");

            // Iterate over each card in the array
            for (int i = 0; i < cardsArray.length(); i++) {
                JSONObject cardJson = cardsArray.getJSONObject(i);

                // Get the criteria object from the card JSON
                JSONObject criteriaObj = cardJson.getJSONObject("criteria");

                // Add each vegetable card to the deck with its corresponding criteria
                deckPepper.add(new card(card.Vegetable.PEPPER, criteriaObj.getString("PEPPER")));
                deckLettuce.add(new card(card.Vegetable.LETTUCE, criteriaObj.getString("LETTUCE")));
                deckCarrot.add(new card(card.Vegetable.CARROT, criteriaObj.getString("CARROT")));
                deckCabbage.add(new card(card.Vegetable.CABBAGE, criteriaObj.getString("CABBAGE")));
                deckOnion.add(new card(card.Vegetable.ONION, criteriaObj.getString("ONION")));
                deckTomato.add(new card(card.Vegetable.TOMATO, criteriaObj.getString("TOMATO")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Shuffle each deck
		shuffleDeck(deckPepper);
		shuffleDeck(deckLettuce);
		shuffleDeck(deckCarrot);
		shuffleDeck(deckCabbage);
		shuffleDeck(deckOnion);
		shuffleDeck(deckTomato);

		System.out.println(nrPlayers);
		int cardsPerVeggie = nrPlayers/2 * 6;
		
		
		for(int i = 0; i < cardsPerVeggie; i++) {
			deck.add(deckPepper.remove(0));
			deck.add(deckLettuce.remove(0));
			deck.add(deckCarrot.remove(0));
			deck.add(deckCabbage.remove(0));
			deck.add(deckOnion.remove(0));
			deck.add(deckTomato.remove(0));
		}
		shuffleDeck(deck);

		//divide the deck into 3 piles
		ArrayList<card> pile1 = new ArrayList<>();
		ArrayList<card> pile2 = new ArrayList<>();
		ArrayList<card> pile3 = new ArrayList<>();
		for (int i = 0; i < deck.size(); i++) {
			if (i % 3 == 0) {
				pile1.add(deck.get(i));
			} else if (i % 3 == 1) {
				pile2.add(deck.get(i));
			} else {
				pile3.add(deck.get(i));
			}
		}
        biggestPileIndex();
		piles.add(new pile(pile1,biggestPileIndex,biggestSize));
		piles.add(new pile(pile2,biggestPileIndex,biggestSize));
		piles.add(new pile(pile3,biggestPileIndex,biggestSize));
    }
    private void shuffleDeck(ArrayList<card> deck) {
		Collections.shuffle(deck);
	}
    
    

}

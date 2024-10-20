package game.piles;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class SetPileSalad extends PileHandler {
    private ArrayList<Pile> piles = new ArrayList<>();

    public SetPileSalad(int nrPlayers) {
        Pile deckPepper = new Pile();
        Pile deckLettuce = new Pile();
        Pile deckCarrot = new Pile();
        Pile deckCabbage = new Pile();
        Pile deckOnion = new Pile();
        Pile deckTomato = new Pile();

        loadCards(deckPepper, deckLettuce, deckCarrot, deckCabbage, deckOnion, deckTomato);
        shuffleDecks(deckPepper, deckLettuce, deckCarrot, deckCabbage, deckOnion, deckTomato);

        int cardsPerVeggie;
        switch (nrPlayers) {
            case 2:
            cardsPerVeggie = 6;
            break;
            case 3:
            cardsPerVeggie = 9;
            break;
            case 4:
            cardsPerVeggie = 12; // Assuming the deck has 18 of each veggie, removing 6 leaves 12
            break;
            case 5:
            cardsPerVeggie = 15; // Assuming the deck has 18 of each veggie, removing 3 leaves 15
            break;
            case 6:
            cardsPerVeggie = 18; // Use the entire deck
            break;
            default:
            throw new IllegalArgumentException("Invalid number of players: " + nrPlayers);
        }
        Pile deck = createCombinedDeck(deckPepper, deckLettuce, deckCarrot, deckCabbage, deckOnion, deckTomato, cardsPerVeggie);
        deck.shuffleDeck();

        distributeCards(deck);
    }



    private void loadCards(Pile deckPepper, Pile deckLettuce, Pile deckCarrot, Pile deckCabbage, Pile deckOnion, Pile deckTomato) {
        try (InputStream fInputStream = new FileInputStream("PointSaladManifest.json");
                Scanner scanner = new Scanner(fInputStream, "UTF-8").useDelimiter("\\A")) {

            String jsonString = scanner.hasNext() ? scanner.next() : "";
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray cardsArray = jsonObject.getJSONArray("cards");

            for (int i = 0; i < cardsArray.length(); i++) {
                JSONObject cardJson = cardsArray.getJSONObject(i);
                JSONObject criteriaObj = cardJson.getJSONObject("criteria");

                deckPepper.addCard(new CardSalad(CardSalad.Vegetable.PEPPER, criteriaObj.getString("PEPPER")));
                deckLettuce.addCard(new CardSalad(CardSalad.Vegetable.LETTUCE, criteriaObj.getString("LETTUCE")));
                deckCarrot.addCard(new CardSalad(CardSalad.Vegetable.CARROT, criteriaObj.getString("CARROT")));
                deckCabbage.addCard(new CardSalad(CardSalad.Vegetable.CABBAGE, criteriaObj.getString("CABBAGE")));
                deckOnion.addCard(new CardSalad(CardSalad.Vegetable.ONION, criteriaObj.getString("ONION")));
                deckTomato.addCard(new CardSalad(CardSalad.Vegetable.TOMATO, criteriaObj.getString("TOMATO")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shuffleDecks(Pile... decks) {
        for (Pile deck : decks) {
            deck.shuffleDeck();
        }
    }

    private Pile createCombinedDeck(Pile deckPepper, Pile deckLettuce, Pile deckCarrot, Pile deckCabbage, Pile deckOnion, Pile deckTomato, int cardsPerVeggie) {
        Pile deck = new Pile();
        for (int i = 0; i < cardsPerVeggie; i++) {
            deck.addCard(deckPepper.buyCard(0));
            deck.addCard(deckLettuce.buyCard(0));
            deck.addCard(deckCarrot.buyCard(0));
            deck.addCard(deckCabbage.buyCard(0));
            deck.addCard(deckOnion.buyCard(0));
            deck.addCard(deckTomato.buyCard(0));
        }
        return deck;
    }

    private void distributeCards(Pile deck) {
        Pile pile1 = new Pile();
        Pile pile2 = new Pile();
        Pile pile3 = new Pile();
        int pileSize = deck.getSize() / 3;
        for (int i = 0; i < pileSize; i++) {
            pile1.addCard(deck.buyCard(0));
            pile2.addCard(deck.buyCard(0));
            pile3.addCard(deck.buyCard(0));
        }
        
        piles.add(pile1);
        piles.add(pile2);
        piles.add(pile3);
    }

    public Pile getPile(int pileIndex) {
        return piles.get(pileIndex);
    }

    public ArrayList<Pile> getPiles() {
        return piles;
    }

    public boolean isEmpty() {
        return piles.isEmpty();
    }
}
   

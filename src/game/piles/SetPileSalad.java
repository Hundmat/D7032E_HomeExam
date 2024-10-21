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

    /**
     * Constructs a SetPileSalad object for a given number of players.
     * Initializes and shuffles individual vegetable decks, combines them into a single deck,
     * and distributes the cards accordingly.
     *
     * @param nrPlayers the number of players in the game. Must be between 2 and 6 inclusive.
     * @throws IllegalArgumentException if the number of players is not between 2 and 6.
     */
    public SetPileSalad(int nrPlayers) {
        super(nrPlayers);
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



    /**
     * Loads cards from a JSON file and populates the provided piles with the corresponding cards.
     *
     * @param deckPepper  the pile to be populated with PEPPER cards
     * @param deckLettuce the pile to be populated with LETTUCE cards
     * @param deckCarrot  the pile to be populated with CARROT cards
     * @param deckCabbage the pile to be populated with CABBAGE cards
     * @param deckOnion   the pile to be populated with ONION cards
     * @param deckTomato  the pile to be populated with TOMATO cards
     */
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

    /**
     * Creates a combined deck from multiple vegetable decks.
     *
     * @param deckPepper The deck containing pepper cards.
     * @param deckLettuce The deck containing lettuce cards.
     * @param deckCarrot The deck containing carrot cards.
     * @param deckCabbage The deck containing cabbage cards.
     * @param deckOnion The deck containing onion cards.
     * @param deckTomato The deck containing tomato cards.
     * @param cardsPerVeggie The number of cards to take from each vegetable deck.
     * @return A new combined deck containing cards from all the specified vegetable decks.
     */
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
        
        System.out.println("Pile 1: " + pile1.getSize());
        System.out.println("Pile 2: " + pile2.getSize());
        System.out.println("Pile 3: " + pile3.getSize());
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
   

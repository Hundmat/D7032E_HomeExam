

import java.util.Collections;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import network.Client;
import network.MessageListener;
import network.ServerTest;

public class PointSalad implements MessageListener {

    private ArrayList<Pile> piles = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

	


	private Client client;
    private int numberPlayers;
	private int PlayerID;
	private long seed;
    private boolean isBot;
    private boolean turn;
    

    class Player {
        public int playerID;
        public boolean online;
        public boolean isBot;
        public Socket connection;
        public ObjectInputStream inFromClient;
        public ObjectOutputStream outToClient;
        public ArrayList<String> region = new ArrayList<String>();
        Scanner in = new Scanner(System.in);
        public ArrayList<Card> hand = new ArrayList<Card>();
        public int score = 0;

        public Player(int playerID, boolean isBot, Socket connection, ObjectInputStream inFromClient,
                ObjectOutputStream outToClient) {
            this.playerID = playerID;
            this.connection = connection;
            this.inFromClient = inFromClient;
            this.outToClient = outToClient;
            this.isBot = isBot;
            if (connection == null)
                this.online = false;
            else
                this.online = true;
        }

        public void sendMessage(Object message) {
            if (online) {
                try {
                    outToClient.writeObject(message);
                } catch (Exception e) {
                }
            } else if (!isBot) {
                System.out.println(message);
            }
        }

        public String readMessage() {
            String word = "";
            if (online)
                try {
                    word = (String) inFromClient.readObject();
                } catch (Exception e) {
                }
            else
                try {
                    word = in.nextLine();
                } catch (Exception e) {
                }
            return word;
        }
    }

    public class Pile {
        public ArrayList<Card> cards = new ArrayList<Card>();
        public Card[] veggieCards = new Card[2];

        public Pile(ArrayList<Card> cards) {
            this.cards = cards;
            this.veggieCards[0] = cards.remove(0);
            this.veggieCards[1] = cards.remove(0);
            this.veggieCards[0].criteriaSideUp = false;
            this.veggieCards[1].criteriaSideUp = false;
        }

        public Card getPointCard() {
            if (cards.isEmpty()) {
                // remove from the bottom of the biggest of the other piles
                int biggestPileIndex = 0;
                int biggestSize = 0;
                for (int i = 0; i < piles.size(); i++) {
                    if (i != piles.indexOf(this) && piles.get(i).cards.size() > biggestSize) {
                        biggestSize = piles.get(i).cards.size();
                        biggestPileIndex = i;
                    }
                }
                if (biggestSize > 1) {
                    cards.add(piles.get(biggestPileIndex).cards.remove(piles.get(biggestPileIndex).cards.size() - 1));
                } else // we can't remove active point cards from other piles
                    return null;
            }
            return cards.get(0);
        }

        public Card buyPointCard() {
            if (cards.isEmpty()) {
                // remove from the bottom of the biggest of the other piles
                int biggestPileIndex = 0;
                int biggestSize = 0;
                for (int i = 0; i < piles.size(); i++) {
                    if (i != piles.indexOf(this) && piles.get(i).cards.size() > biggestSize) {
                        biggestSize = piles.get(i).cards.size();
                        biggestPileIndex = i;
                    }
                }
                if (biggestSize > 1) {
                    cards.add(piles.get(biggestPileIndex).cards.remove(piles.get(biggestPileIndex).cards.size() - 1));
                } else { // we can't remove active point cards from other piles
                    return null;
                }
            }
            return cards.remove(0);
        }

        public Card getVeggieCard(int index) {
            return veggieCards[index];
        }

        public Card buyVeggieCard(int index) {
            Card aCard = veggieCards[index];
            if (cards.size() <= 1) {
                // remove from the bottom of the biggest of the other piles
                int biggestPileIndex = 0;
                int biggestSize = 0;
                for (int i = 0; i < piles.size(); i++) {
                    if (i != piles.indexOf(this) && piles.get(i).cards.size() > biggestSize) {
                        biggestSize = piles.get(i).cards.size();
                        biggestPileIndex = i;
                    }
                }
                if (biggestSize > 1) {
                    cards.add(piles.get(biggestPileIndex).cards.remove(piles.get(biggestPileIndex).cards.size() - 1));
                    veggieCards[index] = cards.remove(0);
                    veggieCards[index].criteriaSideUp = false;
                } else {
                    veggieCards[index] = null;
                }
            } else {
                veggieCards[index] = cards.remove(0);
                veggieCards[index].criteriaSideUp = false;
            }

            return aCard;
        }

        public boolean isEmpty() {
            return cards.isEmpty() && veggieCards[0] == null && veggieCards[1] == null;
        }
    }

    public static class Card {
        public enum Vegetable {
            PEPPER, LETTUCE, CARROT, CABBAGE, ONION, TOMATO
        }

        public Vegetable vegetable;
        public String criteria;
        public boolean criteriaSideUp = true;

        public Card(Vegetable vegetable, String criteria) {
            this.vegetable = vegetable;
            this.criteria = criteria;
        }

        @Override
        public String toString() {
            if (criteriaSideUp) {
                return criteria + " (" + vegetable + ")";
            } else {
                return vegetable.toString();
            }
        }
    }

    public void setPiles(int nrPlayers) {
        ArrayList<Card> deckPepper = new ArrayList<>();
        ArrayList<Card> deckLettuce = new ArrayList<>();
        ArrayList<Card> deckCarrot = new ArrayList<>();
        ArrayList<Card> deckCabbage = new ArrayList<>();
        ArrayList<Card> deckOnion = new ArrayList<>();
        ArrayList<Card> deckTomato = new ArrayList<>();

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
                deckPepper.add(new Card(Card.Vegetable.PEPPER, criteriaObj.getString("PEPPER")));
                deckLettuce.add(new Card(Card.Vegetable.LETTUCE, criteriaObj.getString("LETTUCE")));
                deckCarrot.add(new Card(Card.Vegetable.CARROT, criteriaObj.getString("CARROT")));
                deckCabbage.add(new Card(Card.Vegetable.CABBAGE, criteriaObj.getString("CABBAGE")));
                deckOnion.add(new Card(Card.Vegetable.ONION, criteriaObj.getString("ONION")));
                deckTomato.add(new Card(Card.Vegetable.TOMATO, criteriaObj.getString("TOMATO")));
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

        int cardsPerVeggie = nrPlayers / 2 * 6;

        ArrayList<Card> deck = new ArrayList<>();
        for (int i = 0; i < cardsPerVeggie; i++) {
            deck.add(deckPepper.remove(0));
            deck.add(deckLettuce.remove(0));
            deck.add(deckCarrot.remove(0));
            deck.add(deckCabbage.remove(0));
            deck.add(deckOnion.remove(0));
            deck.add(deckTomato.remove(0));
        }
        shuffleDeck(deck);

        // divide the deck into 3 piles
        ArrayList<Card> pile1 = new ArrayList<>();
        ArrayList<Card> pile2 = new ArrayList<>();
        ArrayList<Card> pile3 = new ArrayList<>();
        for (int i = 0; i < deck.size(); i++) {
            if (i % 3 == 0) {
                pile1.add(deck.get(i));
            } else if (i % 3 == 1) {
                pile2.add(deck.get(i));
            } else {
                pile3.add(deck.get(i));
            }
        }
        piles.add(new Pile(pile1));
        piles.add(new Pile(pile2));
        piles.add(new Pile(pile3));
    }

    public int calculateScore(ArrayList<Card> hand, Player thisPlayer) {
        // System.out.println("DEBUG: \n" + displayHand(hand));
        int totalScore = 0;

        for (Card criteriaCard : hand) {
            if (criteriaCard.criteriaSideUp) {
                String criteria = criteriaCard.criteria;
                String[] parts = criteria.split(",");

                // ID 18
                if (criteria.indexOf("TOTAL") >= 0 || criteria.indexOf("TYPE") >= 0 || criteria.indexOf("SET") >= 0) {
                    if (criteria.indexOf("TOTAL") >= 0) {
                        int countVeg = countTotalVegetables(hand);
                        int thisHandCount = countVeg;
                        for (Player p : players) {
                            if (p.playerID != thisPlayer.playerID) {
                                int playerVeg = countTotalVegetables(p.hand);
                                if ((criteria.indexOf("MOST") >= 0) && (playerVeg > countVeg)) {
                                    countVeg = countTotalVegetables(p.hand);
                                }
                                if ((criteria.indexOf("FEWEST") >= 0) && (playerVeg < countVeg)) {
                                    countVeg = countTotalVegetables(p.hand);
                                }
                            }
                        }
                        if (countVeg == thisHandCount) {
                            // int aScore =
                            // Integer.parseInt(criteria.substring(criteria.indexOf("=")+1).trim());
                            // System.out.print("ID18 MOST/FEWEST: "+aScore + " " );
                            totalScore += Integer.parseInt(criteria.substring(criteria.indexOf("=") + 1).trim());
                        }
                    }
                    if (criteria.indexOf("TYPE") >= 0) {
                        String[] expr = criteria.split("/");
                        int addScore = Integer.parseInt(expr[0].trim());
                        if (expr[1].indexOf("MISSING") >= 0) {
                            int missing = 0;
                            for (Card.Vegetable vegetable : Card.Vegetable.values()) {
                                if (countVegetables(hand, vegetable) == 0) {
                                    missing++;
                                }
                            }
                            // int aScore = missing * addScore;
                            // System.out.print("ID18 TYPE MISSING: "+aScore + " ");
                            totalScore += missing * addScore;
                        } else {
                            int atLeastPerVegType = Integer
                                    .parseInt(expr[1].substring(expr[1].indexOf(">=") + 2).trim());
                            int totalType = 0;
                            for (Card.Vegetable vegetable : Card.Vegetable.values()) {
                                int countVeg = countVegetables(hand, vegetable);
                                if (countVeg >= atLeastPerVegType) {
                                    totalType++;
                                }
                            }
                            // int aScore = totalType * addScore;
                            // System.out.print("ID18 TYPE >=: "+aScore + " ");
                            totalScore += totalType * addScore;
                        }
                    }
                    if (criteria.indexOf("SET") >= 0) {
                        int addScore = 12;
                        for (Card.Vegetable vegetable : Card.Vegetable.values()) {
                            int countVeg = countVegetables(hand, vegetable);
                            if (countVeg == 0) {
                                addScore = 0;
                                break;
                            }
                        }
                        // System.out.print("ID18 SET: "+addScore + " ");
                        totalScore += addScore;
                    }
                }
                // ID1 and ID2
                else if ((criteria.indexOf("MOST") >= 0) || (criteria.indexOf("FEWEST") >= 0)) { // ID1, ID2
                    int vegIndex = criteria.indexOf("MOST") >= 0 ? criteria.indexOf("MOST") + 5
                            : criteria.indexOf("FEWEST") + 7;
                    String veg = criteria.substring(vegIndex, criteria.indexOf("=")).trim();
                    int countVeg = countVegetables(hand, Card.Vegetable.valueOf(veg));
                    int nrVeg = countVeg;
                    for (Player p : players) {
                        if (p.playerID != thisPlayer.playerID) {
                            int playerVeg = countVegetables(p.hand, Card.Vegetable.valueOf(veg));
                            if ((criteria.indexOf("MOST") >= 0) && (playerVeg > nrVeg)) {
                                nrVeg = countVegetables(p.hand, Card.Vegetable.valueOf(veg));
                            }
                            if ((criteria.indexOf("FEWEST") >= 0) && (playerVeg < nrVeg)) {
                                nrVeg = countVegetables(p.hand, Card.Vegetable.valueOf(veg));
                            }
                        }
                    }
                    if (nrVeg == countVeg) {
                        // System.out.print("ID1/ID2:
                        // "+Integer.parseInt(criteria.substring(criteria.indexOf("=")+1).trim()) + "
                        // ");
                        totalScore += Integer.parseInt(criteria.substring(criteria.indexOf("=") + 1).trim());
                    }
                }

                // ID3, ID4, ID5, ID6, ID7, ID8, ID9, ID10, ID11, ID12, ID13, ID14, ID15, ID16,
                // ID17
                else if (parts.length > 1 || criteria.indexOf("+") >= 0 || parts[0].indexOf("/") >= 0) {
                    if (criteria.indexOf("+") >= 0) { // ID5, ID6, ID7, ID11, ID12, ID13
                        String expr = criteria.split("=")[0].trim();
                        String[] vegs = expr.split("\\+");
                        int[] nrVeg = new int[vegs.length];
                        int countSameKind = 1;
                        for (int j = 1; j < vegs.length; j++) {
                            if (vegs[0].trim().equals(vegs[j].trim())) {
                                countSameKind++;
                            }
                        }
                        if (countSameKind > 1) {
                            // System.out.print("ID5/ID11: "+ ((int)countVegetables(hand,
                            // Card.Vegetable.valueOf(vegs[0].trim()))/countSameKind) *
                            // Integer.parseInt(criteria.split("=")[1].trim()) + " ");
                            totalScore += ((int) countVegetables(hand, Card.Vegetable.valueOf(vegs[0].trim()))
                                    / countSameKind) * Integer.parseInt(criteria.split("=")[1].trim());
                        } else {
                            for (int i = 0; i < vegs.length; i++) {
                                nrVeg[i] = countVegetables(hand, Card.Vegetable.valueOf(vegs[i].trim()));
                            }
                            // find the lowest number in the nrVeg array
                            int min = nrVeg[0];
                            for (int x = 1; x < nrVeg.length; x++) {
                                if (nrVeg[x] < min) {
                                    min = nrVeg[x];
                                }
                            }
                            // System.out.print("ID6/ID7/ID12/ID13: "+min *
                            // Integer.parseInt(criteria.split("=")[1].trim()) + " ");
                            totalScore += min * Integer.parseInt(criteria.split("=")[1].trim());
                        }
                    } else if (parts[0].indexOf("=") >= 0) { // ID3
                        String veg = parts[0].substring(0, parts[0].indexOf(":"));
                        int countVeg = countVegetables(hand, Card.Vegetable.valueOf(veg));
                        // System.out.print("ID3: "+((countVeg%2==0)?7:3) + " ");
                        totalScore += (countVeg % 2 == 0) ? 7 : 3;
                    } else { // ID4, ID8, ID9, ID10, ID14, ID15, ID16, ID17
                        for (int i = 0; i < parts.length; i++) {
                            String[] veg = parts[i].split("/");
                            // System.out.print("ID4/ID8/ID9/ID10/ID14/ID15/ID16/ID17: " +
                            // Integer.parseInt(veg[0].trim()) * countVegetables(hand,
                            // Card.Vegetable.valueOf(veg[1].trim())) + " ");
                            totalScore += Integer.parseInt(veg[0].trim())
                                    * countVegetables(hand, Card.Vegetable.valueOf(veg[1].trim()));
                        }
                    }
                }
            }
        }
        return totalScore;
    }

    private int countVegetables(ArrayList<Card> hand, Card.Vegetable vegetable) {
        int count = 0;
        for (Card card : hand) {
            if (!card.criteriaSideUp && card.vegetable == vegetable) {
                count++;
            }
        }
        return count;
    }

    private int countTotalVegetables(ArrayList<Card> hand) {
        int count = 0;
        for (Card card : hand) {
            if (!card.criteriaSideUp) {
                count++;
            }
        }
        return count;
    }

    public void shuffleDeck(ArrayList<Card> deck) {
        Collections.shuffle(deck);
    }

    public String displayHand(ArrayList<Card> hand) {
        String handString = "Criteria:\t";
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).criteriaSideUp && hand.get(i).vegetable != null) {
                handString += "[" + i + "] " + hand.get(i).criteria + " (" + hand.get(i).vegetable.toString() + ")"
                        + "\t";
            }
        }
        handString += "\nVegetables:\t";
        // Sum up the number of each vegetable and show the total number of each
        // vegetable
        for (Card.Vegetable vegetable : Card.Vegetable.values()) {
            int count = countVegetables(hand, vegetable);
            if (count > 0) {
                handString += vegetable + ": " + count + "\t";
            }
        }
        return handString;
    }

    


    private String printMarket() {
        String pileString = "Point Cards:\t";
        for (int p = 0; p < piles.size(); p++) {
            if (piles.get(p).getPointCard() == null) {
                pileString += "[" + p + "]" + String.format("%-43s", "Empty") + "\t";
            } else
                pileString += "[" + p + "]" + String.format("%-43s", piles.get(p).getPointCard()) + "\t";
        }
        pileString += "\nVeggie Cards:\t";
        char veggieCardIndex = 'A';
        for (Pile pile : piles) {
            pileString += "[" + veggieCardIndex + "]" + String.format("%-43s", pile.getVeggieCard(0)) + "\t";
            veggieCardIndex++;
        }
        pileString += "\n\t\t";
        for (Pile pile : piles) {
            pileString += "[" + veggieCardIndex + "]" + String.format("%-43s", pile.getVeggieCard(1)) + "\t";
            veggieCardIndex++;
        }
        return pileString;
    }

    public PointSalad(String[] args) {
        
		try {
			this.client = new Client(args[0],this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

        
		client.sendMessage("this is from the client");
        // Set random starting player
      
        boolean keepPlaying = true;

        while (keepPlaying) {
		};
    }

	public void onUpdate(String updateMessage) {
		if (updateMessage.contains("amountofplayers")) {
            JSONObject updateJson = new JSONObject(updateMessage);
			this.PlayerID = updateJson.getInt("playerID");
			this.seed = updateJson.getLong("seed");
			this.numberPlayers = updateJson.getInt("amountofplayers");
			this.isBot = updateJson.getBoolean("isBot");
			this.turn = updateJson.getBoolean("isStartPlayer");
        }
		System.out.println("palyerID = " + this.PlayerID);
		System.out.println("seed = " + this.seed);
		System.out.println("numberPlayers = " + this.numberPlayers);
		System.out.println("isBot = " + this.isBot);
		System.out.println("turn = " + this.turn);

	}

	
    public static void main(String[] args) {
        PointSalad game = new PointSalad(args);

    }
}
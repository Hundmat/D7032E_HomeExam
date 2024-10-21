package game.players;

import game.piles.PileHandler;

import java.util.ArrayList;

import game.piles.Card;
import game.piles.Pile;



public class PlayerHand extends PileHandler {
    private ArrayList<Pile> playerHands = new ArrayList<>();
    public PlayerHand(int playerSize) {
        super(playerSize);
        for (int i = 0; i < playerSize; i++) {
            playerHands.add(new Pile());
        }
    }

    

   

    public String displayHand(int playerID) {
        String handString = "Criteria Cards:\t";
        Pile activeHand = playerHands.get(playerID);
		for (int i = 0; i < activeHand.getSize(); i++) {
			if(activeHand.getCard(i).isCriteriaSideUp() && activeHand.getCard(i).getCardType() != null) {
				handString += "["+i+"] "+activeHand.getCard(i).getCriteria() + " ("+activeHand.getCard(i).getCardType().toString()+")"+"\t";
			}
		}
		handString += "\nType Cards:\t";
		//Sum up the number of each vegetable and show the total number of each vegetable
        ArrayList<Enum<?>> types = new ArrayList<>();
        for (int i = 0; i < activeHand.getSize(); i++) {
            Enum<?> activeEnum = activeHand.getCard(i).getCardType();
            if(!types.contains(activeEnum)) {
                int count = countTotalofOneTypeCard(playerID, activeEnum);
                if(count > 0) {
                    handString += activeEnum + ": " + count + "\t";
                }
                types.add(activeHand.getCard(i).getCardType());
            } else {
                continue;
            }
        }
		
		return handString;
    }

    public int countTotalofOneTypeCard(int playerID, Enum<?> type) {
		int count = 0;
        Pile activeHand = playerHands.get(playerID);
        for (int i = 0; i < activeHand.getSize(); i++) {
            if (activeHand.getCard(i).getCardType() == type && !activeHand.getCard(i).isCriteriaSideUp()) {
                count++;
            }
			
		}
		
		return count;
	}

    public int countTotalTypeCard(int playerID) {
        int count = 0;
        Pile activeHand = playerHands.get(playerID);
        for (int i = 0; i < activeHand.getSize(); i++) {
            if (!activeHand.getCard(i).isCriteriaSideUp()) {
                count++;
            }
        }
        return count;
    }

    public void addCard(int playerID, Card card) {
        playerHands.get(playerID).addCard(card);
    }

    public Pile getPile(int pileIndex) {
        return playerHands.get(pileIndex);
    }

    public ArrayList<Pile> getPiles() {
        return playerHands;
    }

    public boolean isEmpty() {
        return playerHands.isEmpty();
    }
}

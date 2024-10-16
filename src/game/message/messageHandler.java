package game;

import java.util.ArrayList;

import javax.smartcardio.Card;

public class messageHandler {



    public void sendMessage(Object message) {
        if(online) {
            try {outToClient.writeObject(message);} catch (Exception e) {}
        } else if(!isBot){
            System.out.println(message);                
        }
    }

    private String printMarket() {
		String pileString = "Point Cards:\t";
		for (int p=0; p<piles.size(); p++) {
			if(piles.get(p).getPointCard()==null) {
				pileString += "["+p+"]"+String.format("%-43s", "Empty") + "\t";
			}
			else
				pileString += "["+p+"]"+String.format("%-43s", piles.get(p).getPointCard()) + "\t";
		}
		pileString += "\nVeggie Cards:\t";
		char veggieCardIndex = 'A';
		for (Pile pile : piles) {
			pileString += "["+veggieCardIndex+"]"+String.format("%-43s", pile.getVeggieCard(0)) + "\t";
			veggieCardIndex++;
		}
		pileString += "\n\t\t";
		for (Pile pile : piles) {
			pileString += "["+veggieCardIndex+"]"+String.format("%-43s", pile.getVeggieCard(1)) + "\t";
			veggieCardIndex++;
		}
		return pileString;
	}

    public String displayHand(ArrayList<Card> hand) {
		String handString = "Criteria:\t";
		for (int i = 0; i < hand.size(); i++) {
			if(hand.get(i).criteriaSideUp && hand.get(i).vegetable != null) {
				handString += "["+i+"] "+hand.get(i).criteria + " ("+hand.get(i).vegetable.toString()+")"+"\t";
			}
		}
		handString += "\nVegetables:\t";
		//Sum up the number of each vegetable and show the total number of each vegetable
		for (Card.Vegetable vegetable : Card.Vegetable.values()) {
			int count = countVegetables(hand, vegetable);
			if(count > 0) {
				handString += vegetable + ": " + count + "\t";
			}
		}
		return handString;
	}

	private void sendToAllPlayers(String message) {
		for(Player player : players) {
			player.sendMessage(message);
		}
	}
}

package game.players;

import java.util.ArrayList;

import javax.smartcardio.Card;

public class botLogic {
    
    // Bot logic
    // The Bot will randomly decide to take either one point card or two veggie cards 
    // For point card the Bot will always take the point card with the highest score
    // If there are two point cards with the same score, the bot will take the first one
    // For Veggie cards the Bot will pick the first one or two available veggies
    boolean emptyPiles = false;
    // Random choice: 
    int choice = (int) (Math.random() * 2);
    if(choice == 0) {
        // Take a point card
        int highestPointCardIndex = 0;
        int highestPointCardScore = 0;
        for(int i = 0; i < piles.size(); i++) {
            if(piles.get(i).getPointCard() != null) {
                ArrayList<Card> tempHand = new ArrayList<Card>();
                for(Card handCard : thisPlayer.hand) {
                    tempHand.add(handCard);
                }
                tempHand.add(piles.get(i).getPointCard());
                int score = calculateScore(tempHand, thisPlayer);
                if(score > highestPointCardScore) {
                    highestPointCardScore = score;
                    highestPointCardIndex = i;
                }
            }
        }
        if(piles.get(highestPointCardIndex).getPointCard() != null) {
            thisPlayer.hand.add(piles.get(highestPointCardIndex).buyPointCard());
        } else {
            choice = 1; //buy veggies instead
            emptyPiles = true;
        }
    } else if (choice == 1) {
        // TODO: Check what Veggies are available and run calculateScore to see which veggies are best to pick
        int cardsPicked = 0;
        for(Pile pile : piles) {
            if(pile.veggieCards[0] != null && cardsPicked < 2) {
                thisPlayer.hand.add(pile.buyVeggieCard(0));
                cardsPicked++;
            }
            if(pile.veggieCards[1] != null && cardsPicked < 2) {
                thisPlayer.hand.add(pile.buyVeggieCard(1));
                cardsPicked++;
            }
        }
        if(cardsPicked == 0 && !emptyPiles) {
            // Take a point card instead of veggies if there are no veggies left
            int highestPointCardIndex = 0;
            int highestPointCardScore = 0;
            for(int i = 0; i < piles.size(); i++) {
                if(piles.get(i).getPointCard() != null && piles.get(i).getPointCard().criteriaSideUp) {
                    ArrayList<Card> tempHand = new ArrayList<Card>();
                    for(Card handCard : thisPlayer.hand) {
                        tempHand.add(handCard);
                    }
                    tempHand.add(piles.get(i).getPointCard());
                    int score = calculateScore(tempHand, thisPlayer);
                    if(score > highestPointCardScore) {
                        highestPointCardScore = score;
                        highestPointCardIndex = i;
                    }
                }
            }
            if(piles.get(highestPointCardIndex).getPointCard() != null) {
                thisPlayer.hand.add(piles.get(highestPointCardIndex).buyPointCard());
            }
        }
    }		
}

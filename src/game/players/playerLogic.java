package game.players;

import java.util.ArrayList;

import game.cards.pilesLogic;
import game.message.messageHandler;

public class playerLogic {
    public ArrayList<player> players = new ArrayList<>();
    messageHandler messageHandler = new messageHandler();

    public playerLogic(player thisPlayer, messageHandler messageHandler, pilesLogic piles) {
        this.thisPlayer = thisPlayer;
    }
    


    thisPlayer.sendMessage("\n\n****************************************************************\nIt's your turn! Your hand is:\n");
    thisPlayer.sendMessage(displayHand(thisPlayer.hand));
    thisPlayer.sendMessage("\nThe piles are: ");

    thisPlayer.sendMessage(printMarket());
    boolean validChoice = false;
    while(!validChoice) {
        thisPlayer.sendMessage("\n\nTake either one point card (Syntax example: 2) or up to two vegetable cards (Syntax example: CF).\n");
        String pileChoice = thisPlayer.readMessage();
        if(pileChoice.matches("\\d")) {
            int pileIndex = Integer.parseInt(pileChoice);
            if(piles.get(pileIndex).getPointCard() == null) {
                thisPlayer.sendMessage("\nThis pile is empty. Please choose another pile.\n");
                continue;
            } else {
                thisPlayer.hand.add(piles.get(pileIndex).buyPointCard());
                thisPlayer.sendMessage("\nYou took a card from pile " + (pileIndex) + " and added it to your hand.\n");
                validChoice = true;
            }
        } else {
            int takenVeggies = 0;
            for(int charIndex = 0; charIndex < pileChoice.length(); charIndex++) {
                if(Character.toUpperCase(pileChoice.charAt(charIndex)) < 'A' || Character.toUpperCase(pileChoice.charAt(charIndex)) > 'F') {
                    thisPlayer.sendMessage("\nInvalid choice. Please choose up to two veggie cards from the market.\n");
                    validChoice = false;
                    break;
                }
                int choice = Character.toUpperCase(pileChoice.charAt(charIndex)) - 'A';
                int pileIndex = (choice == 0 || choice == 3) ? 0 : (choice == 1 || choice == 4) ? 1 : (choice == 2 || choice == 5) ? 2:-1;
                int veggieIndex = (choice == 0 || choice == 1 || choice == 2) ? 0 : (choice == 3 || choice == 4 || choice == 5) ? 1 : -1;
                if(piles.get(pileIndex).veggieCards[veggieIndex] == null) {
                    thisPlayer.sendMessage("\nThis veggie is empty. Please choose another pile.\n");
                    validChoice = false;
                    break;
                } else {
                    if(takenVeggies == 2) {
                        validChoice = true;
                        break;
                    } else {
                        thisPlayer.hand.add(piles.get(pileIndex).buyVeggieCard(veggieIndex));
                        takenVeggies++;
                        //thisPlayer.sendMessage("\nYou took a card from pile " + (pileIndex) + " and added it to your hand.\n");
                        validChoice = true;
                    }
                }
            }

        }
    }
    //Check if the player has any criteria cards in their hand
    boolean criteriaCardInHand = false;
    for(Card card : thisPlayer.hand) {
        if(card.criteriaSideUp) {
            criteriaCardInHand = true;
            break;
        }
    }
    if(criteriaCardInHand) {
        //Give the player an option to turn a criteria card into a veggie card
        thisPlayer.sendMessage("\n"+displayHand(thisPlayer.hand)+"\nWould you like to turn a criteria card into a veggie card? (Syntax example: n or 2)");
        String choice = thisPlayer.readMessage();
        if(choice.matches("\\d")) {
            int cardIndex = Integer.parseInt(choice);
            thisPlayer.hand.get(cardIndex).criteriaSideUp = false;
        }
    }
    thisPlayer.sendMessage("\nYour turn is completed\n****************************************************************\n\n");
    sendToAllPlayers("Player " + thisPlayer.playerID + "'s hand is now: \n"+displayHand(thisPlayer.hand)+"\n");
}

package game.gameLogic;

import game.market.MarketPile;
import game.market.MarketPrinterSalad;
import game.market.RefileMarketSalad;
import game.bot.BotLogicSalad;
import game.piles.Card;
import game.piles.Pile;
import game.players.Player;
import game.players.PlayerHand;
import game.piles.SetPileSalad;

import java.util.ArrayList;

public class GameLoopSalad extends GameLoop{
    MarketPrinterSalad marketPrinter;
    SetPileSalad piles;
    PlayerHand playerHand;
    ArrayList<Player> players;
    MarketPile market;
    int currentPlayer;
    boolean keepPlaying;
    Player activePlayer;
    RefileMarketSalad refiller;
    Player thisPlayer;
    SendToAllPlayers sender;
   

    public GameLoopSalad( ArrayList<Player> players, SetPileSalad piles,PlayerHand playerHand,MarketPile market,MarketPrinterSalad marketPrinter,RefileMarketSalad refiller){
       
        super(players, piles, playerHand, market, marketPrinter, refiller);
        this.currentPlayer = (int) (Math.random() * (players.size()));
        
        this.sender = new SendToAllPlayers(players);
        refiller.run();
    }

    

    public void runLoop(){
        boolean keepPlaying = true;
        while(keepPlaying) {
            keepPlaying = checkIfContinue();
            if(!keepPlaying){
                break;
            }
			Player thisPlayer = players.get(currentPlayer);
            this.thisPlayer = thisPlayer;
            this.thisPlayer.sendMessage("It's your turn! Your hand is:\n");
			
            
            
			if(!this.thisPlayer.isBot()) {
				playerAction();
			}else if(this.thisPlayer.isBot()) {
                BotLogicSalad bot = new BotLogicSalad(this.piles, this.market, this.playerHand, this.thisPlayer, this.players,this.refiller);
                bot.run();
                refiller.run();
            }   

            this.sender.sendToAllPlayers("Player " + this.thisPlayer.getPlayerID() + "'s hand is now: \n"+this.playerHand.displayHand(thisPlayer.getPlayerID())+"\n");	

			
			if(currentPlayer == players.size()-1) {
				currentPlayer = 0;
			} else {
				currentPlayer++;
			}
			
		}
    }

    private void playerAction(){
        thisPlayer.sendMessage("\n\n****************************************************************\nIt's your turn! Your hand is:\n");
        thisPlayer.sendMessage(this.playerHand.displayHand(thisPlayer.getPlayerID()));
        thisPlayer.sendMessage("\nThe piles are: ");
        thisPlayer.sendMessage(this.marketPrinter.printMarket(this.piles.getPiles()));
    
        
        boolean validChoice = false;
        while (!validChoice) {
            // Determine whether player can take a point card or only veggie cards
            if (this.refiller.PilesAreEmpty() != 0) {
                thisPlayer.sendMessage("\n\nTake either one point card (Syntax example: 2) or up to two vegetable cards (Syntax example: CF).\n");
            } else {
                thisPlayer.sendMessage("\n\nYou can only take up to two vegetable cards (Syntax example: CF).\n");
            }
        
            // Get the player's input
            String pileChoice = thisPlayer.readMessage();
        
            // Check if the player input is a point card selection and piles are available
            if (pileChoice.matches("\\d") && this.refiller.PilesAreEmpty() != 0) {
                int pileIndex = Integer.parseInt(pileChoice);
                
                // Validate if pileIndex is within the valid range (e.g., 0, 1, 2)
                if (pileIndex < 0 || pileIndex >= piles.getPiles().size()) {
                    thisPlayer.sendMessage("\nInvalid pile choice. Please choose a valid pile (0, 1, or 2).\n");
                    continue;  // Ask the player for input again
                }
            
                // Process the valid pile choice
                if (piles.getPile(pileIndex).isEmpty()) {
                    thisPlayer.sendMessage("\nThis pile is empty. Please choose another pile.\n");
                    continue;  // Ask the player for input again
                } else {
                    this.playerHand.getPile(thisPlayer.getPlayerID()).addCard(piles.getPile(pileIndex).buyCard(0));
                    thisPlayer.sendMessage("\nYou took a card from pile " + pileIndex + " and added it to your hand.\n");
                    validChoice = true;
                }
            }
            else {
                // Handle vegetable card selections
                int takenVeggies = 0;
                validChoice = true;  // Assume valid unless input is invalid
        
                for (int charIndex = 0; charIndex < pileChoice.length(); charIndex++) {
                    char currentChar = Character.toUpperCase(pileChoice.charAt(charIndex));
                    
                    // Validate if the character is between 'A' and 'F'
                    if (currentChar < 'A' || currentChar > 'F') {
                        thisPlayer.sendMessage("\nInvalid choice. Please choose up to two veggie cards from the market.\n");
                        validChoice = false;
                        break;
                    }
        
                    // Only allow up to two veggie selections
                    if (takenVeggies >= 2) {
                        thisPlayer.sendMessage("\nYou can only select up to two veggie cards.\n");
                        validChoice = false;
                        break;
                    }
        
                    int choiceIndex = currentChar - 'A';
                    int pileIndex = -1;
                    int veggieIndex = -1;
        
                    // Determine which pile and which veggie to select based on choiceIndex
                    switch (choiceIndex) {
                        case 0: case 3:
                            pileIndex = 0; veggieIndex = (choiceIndex < 3) ? 0 : 1; break;
                        case 1: case 4:
                            pileIndex = 1; veggieIndex = (choiceIndex < 3) ? 0 : 1; break;
                        case 2: case 5:
                            pileIndex = 2; veggieIndex = (choiceIndex < 3) ? 0 : 1; break;
                        default:
                            thisPlayer.sendMessage("\nInvalid choice. Please choose up to two veggie cards from the market.\n");
                            validChoice = false;
                            break;
                    }
        
                    // Check if the chosen veggie card is available
                    if (pileIndex != -1 && market.getPile(pileIndex).getCard(veggieIndex) == null) {
                        thisPlayer.sendMessage("\nThis veggie is empty. Please choose another pile.\n");
                        validChoice = false;
                        break;
                    }
        
                    // If the input is valid, add the veggie card to the player's hand
                    if (takenVeggies < 2 && validChoice) {
                        playerHand.getPile(thisPlayer.getPlayerID()).addCard(market.buyCard(pileIndex, veggieIndex));
                        takenVeggies++;
                    }
                }
        
                // If two valid veggie cards were taken
                if (validChoice && takenVeggies > 0) {
                    thisPlayer.sendMessage("\nYou have successfully taken " + takenVeggies + " veggie card(s).\n");
                    validChoice = true;  // Valid choice, exit the loop
                } else {
                    validChoice = false;  // Invalid choice, prompt the user again
                }
            }
        
            // If choice was valid, refill the market
            if (validChoice) {
                refiller.run();
            }
        }
        
        boolean criteriaCardInHand = false;
        for (Card card : this.playerHand.getPile(thisPlayer.getPlayerID()).getAll()) {
            if (card.isCriteriaSideUp()) {
                criteriaCardInHand = true;
                validChoice = false;
                break;
            }
        }
        while(!validChoice){
            // Check if the player has any criteria cards in their hand
            

            if (criteriaCardInHand) {
                // Display the player's hand and prompt them for input
                thisPlayer.sendMessage("\n" + this.playerHand.displayHand(thisPlayer.getPlayerID()) + 
                                    "\nWould you like to turn a criteria card into a veggie card? (Syntax example: n or 2)");
                String choice = thisPlayer.readMessage();
                System.out.println("Choice: " + choice);
                if( choice.matches("n")){
                    thisPlayer.sendMessage("\nNo criteria card was rotated\n");
                    validChoice=true;
                    break;
                }
                // Check if the input is a number
                if (choice.matches("\\d") ) {
                    int cardIndex = Integer.parseInt(choice);
                    int playerHandSize = this.playerHand.getPile(thisPlayer.getPlayerID()).getSize();

                    // Check if the cardIndex is within the valid range
                    if (cardIndex >= 0 && cardIndex < playerHandSize) {
                        // Flip the card if the index is valid
                        try {
                            this.playerHand.getPile(this.thisPlayer.getPlayerID()).getCard(cardIndex).flipSide();
                            validChoice=true;
                            break;
                        } catch (Exception e) {
                            thisPlayer.sendMessage("\nInvalid card index. Please choose a valid card number.\n");
                        }
                    } else {
                        // Inform the player if the index is invalid
                        thisPlayer.sendMessage("\nInvalid card index. Please choose a valid card number.\n");
                    }
                } else {
                    thisPlayer.sendMessage("\nInvalid input. Please enter a number.\n");
                }
            }
        }

        thisPlayer.sendMessage("\nYour turn is completed\n****************************************************************\n\n");
    }

    private boolean checkIfContinue(){
        boolean allPilesEmpty = true;
        for(Pile p : piles.getPiles()) {
            if(!p.isEmpty()) {  // If any pile is not empty
                allPilesEmpty = false;  // Set flag to false
                break;  // No need to check further
            }
        }

        if(allPilesEmpty){
            for(Pile hand: market.getPiles()){
                System.out.println(hand.getCard(0));
                System.out.println(hand.getCard(1));
                if(hand.getCard(0)==null && hand.getCard(1)==null){
                    keepPlaying = false;
                    
                }else{
                    keepPlaying = true;
                    return keepPlaying;
                }
            }

            if(!keepPlaying){
                this.sender.sendToAllPlayers("All piles and market are empty. The game has ended!");
                return keepPlaying;
            }
        }
        return true;
    }
}

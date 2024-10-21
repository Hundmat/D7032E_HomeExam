package game;

import game.market.MarketPile;
import game.market.MarketPrinter;
import game.market.RefileMarket;
import game.market.RefileMarketSalad;
import game.bot.BotLogicSalad;
import game.piles.Card;
import game.piles.Pile;
import game.players.Player;
import game.players.PlayerHand;
import game.piles.PileHandler;
import game.piles.SetPileSalad;

import java.util.ArrayList;

public class GameLoopSalad {
    MarketPrinter marketPrinter;
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
   

    public GameLoopSalad( ArrayList<Player> players, SetPileSalad piles,PlayerHand playerHand,MarketPile market,MarketPrinter marketPrinter,RefileMarketSalad refiller){
        this.currentPlayer = (int) (Math.random() * (players.size()));
		this.keepPlaying = true;
        this.players = players;
        this.piles = piles;
		this.playerHand = playerHand;
        this.market = market;
        this.marketPrinter = marketPrinter;
        this.refiller = refiller;
        this.sender = new SendToAllPlayers(players);
        refiller.run();
    }

    

    public void runLoop(){
        while(keepPlaying) {
            
			Player thisPlayer = players.get(currentPlayer);
            this.thisPlayer = thisPlayer;
			boolean allPilesEmpty = true;
            this.thisPlayer.sendMessage("It's your turn! Your hand is:\n");
			
            
            
			if(!this.thisPlayer.isBot()) {
				thisPlayer.sendMessage("\n\n****************************************************************\nIt's your turn! Your hand is:\n");
				thisPlayer.sendMessage(this.playerHand.displayHand(thisPlayer.getPlayerID()));
				thisPlayer.sendMessage("\nThe piles are: ");
                thisPlayer.sendMessage(this.marketPrinter.printMarket(this.piles.getPiles()));
			
				
				boolean validChoice = false;
				while(!validChoice) {
					if(this.refiller.PilesAreEmpty()!=0){
						thisPlayer.sendMessage("\n\nTake either one point card (Syntax example: 2) or up to two vegetable cards (Syntax example: CF).\n");
					}else{
						thisPlayer.sendMessage("\n\nYou can only take up to two vegetable cards (Syntax example: CF).\n");

					}
					String pileChoice = thisPlayer.readMessage();
					if(pileChoice.matches("\\d") && this.refiller.PilesAreEmpty()!=0) {
						int pileIndex = Integer.parseInt(pileChoice);
						if(piles.getPile(pileIndex).isEmpty()) {
							thisPlayer.sendMessage("\nThis pile is empty. Please choose another pile.\n");
							continue;
						} else {
							this.playerHand.getPile(thisPlayer.getPlayerID()).addCard(piles.getPile(pileIndex).buyCard(0));
							this.thisPlayer.sendMessage("\nYou took a card from pile " + (pileIndex) + " and added it to your hand.\n");
							validChoice = true;
						}
					} else {
						int takenVeggies = 0;

						// Loop through each character in the input string
						for (int charIndex = 0; charIndex < pileChoice.length(); charIndex++) {
							char currentChar = Character.toUpperCase(pileChoice.charAt(charIndex));
							
							// Validate input character
							if (currentChar < 'A' || currentChar > 'F') {
								this.thisPlayer.sendMessage("\nInvalid choice. Please choose up to two veggie cards from the market.\n");
								validChoice = false;
								break;
							}

							// Only allow two selections
							if (takenVeggies >= 2) {
								this.thisPlayer.sendMessage("\nYou can only select up to two veggie cards.\n");
								validChoice = false;
								break;
							}

							int choiceIndex = currentChar - 'A';
							int pileIndex = -1;
							int veggieIndex = -1;

							// Determine pileIndex and veggieIndex based on choiceIndex
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

							// Check if the selected pile has cards available
							if (pileIndex != -1 && market.getPile(pileIndex).getCard(veggieIndex) == null) {
								this.thisPlayer.sendMessage("\nThis veggie is empty. Please choose another pile.\n");
								validChoice = false;
								break;
							}

							// Handle adding the veggie card to the player's hand
							if (takenVeggies < 2) {
                                playerHand.getPile(this.thisPlayer.getPlayerID()).addCard(market.buyCard(pileIndex, veggieIndex));
                                takenVeggies++;
								validChoice = true;
								// Uncomment if needed for logging
								// thisPlayer.sendMessage("\nYou took a card from pile " + (pileIndex) + " and added it to your hand.\n");
							}
						}

						// Final message if two cards were taken successfully
						if (validChoice && takenVeggies == 2) {
							thisPlayer.sendMessage("\nYou have successfully taken two veggie cards.\n");
                            
						}
                        refiller.run();

                    }

					
				}
				//Check if the player has any criteria cards in their hand
				boolean criteriaCardInHand = false;
				for(Card card : this.playerHand.getPile(thisPlayer.getPlayerID()).getAll()) 
                {
					if(card.isCriteriaSideUp()) {
						criteriaCardInHand = true;
						break;
					}
				}
				if(criteriaCardInHand) {
					//Give the player an option to turn a criteria card into a veggie card
					thisPlayer.sendMessage("\n"+this.playerHand.displayHand(thisPlayer.getPlayerID())+"\nWould you like to turn a criteria card into a veggie card? (Syntax example: n or 2)");
					String choice = thisPlayer.readMessage();
                    System.out.println("Choice: " + choice);
					if(choice.matches("\\d")) {
						int cardIndex = Integer.parseInt(choice);
						this.playerHand.getPile(this.thisPlayer.getPlayerID()).getCard(cardIndex).flipSide();
                        System.out.println("Card flipped");  
					}
				}
				thisPlayer.sendMessage("\nYour turn is completed\n****************************************************************\n\n");

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
						break;
					}
				}

				if(!keepPlaying){
					this.sender.sendToAllPlayers("All piles and market are empty. The game has ended!");
					break;
				}
			}
			
		}
    }
}

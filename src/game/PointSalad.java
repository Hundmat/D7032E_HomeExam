package game;

import game.bot.BotLogicSalad;
import game.market.*;


import java.util.ArrayList;
import game.players.*;
import game.piles.*;
import game.score.*;


public class PointSalad{
    MarketPrinter marketPrinter;
    SetPileSalad piles;
    PlayerHand playerHand;
    ArrayList<Player> players;
    MarketPile market;
    int currentPlayer;
    boolean keepPlaying;
    Player activePlayer;
    RefileMarket refiller;
    Player thisPlayer;

	


    
    

        
    

        
    

    
    

    private void sendToAllPlayers(String message) {
        for (Player player : this.players) {
            if(!player.isBot()){
                player.sendMessage(message);
            }else{
                System.out.println(message);
            }
            
        }
    }

    public PointSalad(ArrayList<Player> players) {
        System.out.println(players);
        this.players = players;
        this.piles = new SetPileSalad(players.size());
        this.playerHand = new PlayerHand(players.size());
		this.market = new MarketPile(piles.getPiles().size());
        this.marketPrinter = new MarketPrinter(this.market);
        
        this.refiller = new RefileMarket(this.market, this.piles); 
        System.out.println(this.players.get(0).getPlayerID());
        System.out.println(this.players.get(1).getPlayerID());

        refiller.run();
		
        sendToAllPlayers("Welcome to Point Salad!");
        sendToAllPlayers("There are " + players.size() + " players.");
        System.out.println("To server!.");

        int currentPlayer = (int) (Math.random() * (players.size()));
		boolean keepPlaying = true;

        this.currentPlayer = currentPlayer;
        this.keepPlaying = keepPlaying;

        while(keepPlaying) {
            
            System.out.println("Current player: " + currentPlayer);
			Player thisPlayer = players.get(currentPlayer);
            this.thisPlayer = thisPlayer;
			boolean allPilesEmpty = true;
            System.out.println("Player: " + this.thisPlayer.getPlayerID());
            this.thisPlayer.sendMessage("It's your turn! Your hand is:\n");
            for(Pile p : piles.getPiles()) {
                if(!p.isEmpty()) {  // If any pile is not empty
                    allPilesEmpty = false;  // Set flag to false
                    break;  // No need to check further
                }
            }
            if(allPilesEmpty) {  // If all piles are empty, end the game
                keepPlaying = false;
                sendToAllPlayers("All piles are empty. The game has ended!");
                break;
            }
			if(!this.thisPlayer.isBot()) {
				thisPlayer.sendMessage("\n\n****************************************************************\nIt's your turn! Your hand is:\n");
				thisPlayer.sendMessage(this.playerHand.displayHand(thisPlayer.getPlayerID()));
				thisPlayer.sendMessage("\nThe piles are: ");
                thisPlayer.sendMessage(this.marketPrinter.printMarket(this.piles.getPiles()));
			
				
				boolean validChoice = false;
				while(!validChoice) {
					thisPlayer.sendMessage("\n\nTake either one point card (Syntax example: 2) or up to two vegetable cards (Syntax example: CF).\n");
                    
					String pileChoice = thisPlayer.readMessage();
					if(pileChoice.matches("\\d")) {
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
							if (pileIndex != -1 && market.getPile(pileIndex).getCard(takenVeggies) == null) {
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
                BotLogicSalad bot = new BotLogicSalad(this.piles, this.market, this.playerHand, this.thisPlayer, this.players);
                bot.run();
                refiller.run();
            }   

            sendToAllPlayers("Player " + this.thisPlayer.getPlayerID() + "'s hand is now: \n"+this.playerHand.displayHand(thisPlayer.getPlayerID())+"\n");	

			
			if(currentPlayer == players.size()-1) {
				currentPlayer = 0;
			} else {
				currentPlayer++;
			}
		}
   
        sendToAllPlayers(("\n-------------------------------------- CALCULATING SCORES --------------------------------------\n"));
		for(Player player : players) {
			sendToAllPlayers("Player " + player.getPlayerID() + "'s hand is: \n"+playerHand.displayHand(player.getPlayerID()));
            CalculateScore calculateScore = new CalculateScore(playerHand, player.getPlayerID(), players);
			player.setScore(calculateScore.returnScore());
			sendToAllPlayers("\nPlayer " + player.getPlayerID() + "'s score is: " + player.getScore());
		}

		int maxScore = 0;
		int playerID = 0;
		for(Player player : players) {
			if(player.getScore() > maxScore) {
				maxScore = player.getScore();
				playerID = player.getPlayerID();
			}
		}
		for(Player player : players) {
			if(player.getPlayerID() == playerID) {
				player.sendMessage("\nCongratulations! You are the winner with a score of " + maxScore);
			} else {
				player.sendMessage("\nThe winner is player " + playerID + " with a score of " + maxScore);
			}
		}
    }

    
       
}

        
        
        
        

        


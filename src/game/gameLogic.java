package game;

import java.util.ArrayList;
import java.util.Scanner;

import javax.smartcardio.Card;

import game.cards.piles;
import game.cards.pilesLogic;
import game.players.player;
import game.players.playerLogic;


public class gameLogic {
    
   

    public gameLogic() {
        // TODO Auto-generated constructor stub
        int numberplayers = 0;
		int numberOfBots = 0;
		if(args.length == 0) {
			System.out.println("Please enter the number of players (1-6): ");
			Scanner in = new Scanner(System.in);
			numberplayers = in.nextInt();
			System.out.println("Please enter the number of bots (0-5): ");
			numberOfBots = in.nextInt();
		}
		else {
			//check if args[0] is a String (ip address) or an integer (number of players)
			if(args[0].matches("\\d+")) {
				numberplayers = Integer.parseInt(args[0]);
				numberOfBots = Integer.parseInt(args[1]);
			}
			else {
				try {
					client(args[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        public pilesLogic piles = new pilesLogic(numberplayers+numberOfBots);
		piles.setPiles();

		try {
			server(numberplayers, numberOfBots);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set random starting player
		int currentplayer = (int) (Math.random() * (players.size()));
		boolean keepPlaying = true;
    }

    public PointSalad(String[] args) {
		

		while(keepPlaying) {
			player thisplayer = players.get(currentplayer);
			boolean stillAvailableCards = false;
			for(Pile p: piles) {
				if(!p.isEmpty()) {
					stillAvailableCards = true;
					break;
				}
			}
			if(!stillAvailableCards) {
				keepPlaying = false;
				break;
			}
			if(!thisplayer.isBot) {
				//playerLogic	

			} else {
                //botLogic
                sendToAllplayers("Bot " + thisplayer.playerID + "'s hand is now: \n"+displayHand(thisplayer.hand)+"\n");
            }
			
			if(currentplayer == players.size()-1) {
				currentplayer = 0;
			} else {
				currentplayer++;
			}
		}
		sendToAllplayers(("\n-------------------------------------- CALCULATING SCORES --------------------------------------\n"));
		for(player player : players) {
			sendToAllplayers("player " + player.playerID + "'s hand is: \n"+displayHand(player.hand));
			player.score = calculateScore(player.hand, player);
			sendToAllplayers("\nplayer " + player.playerID + "'s score is: " + player.score);
		}

		int maxScore = 0;
		int playerID = 0;
		for(player player : players) {
			if(player.score > maxScore) {
				maxScore = player.score;
				playerID = player.playerID;
			}
		}
		for(player player : players) {
			if(player.playerID == playerID) {
				player.sendMessage("\nCongratulations! You are the winner with a score of " + maxScore);
			} else {
				player.sendMessage("\nThe winner is player " + playerID + " with a score of " + maxScore);
			}
		}
	}

	public static void main(String[] args) {
		PointSalad game = new PointSalad(args);

	}
}

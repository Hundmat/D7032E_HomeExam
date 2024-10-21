package game.gameLogic;

import game.bot.BotLogicSalad;
import game.market.*;


import java.util.ArrayList;
import game.players.*;
import game.piles.*;
import game.score.*;


public class PointSalad extends Game{
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

    public PointSalad(ArrayList<Player> players) {
        super(players);
		
        this.players = players;
        this.piles = new SetPileSalad(players.size());
        this.playerHand = new PlayerHand(players.size());
		this.market = new MarketPile(piles.getPiles().size());
        this.marketPrinter = new MarketPrinterSalad(this.market);
		this.sender = new SendToAllPlayers(players);
        this.refiller = new RefileMarketSalad(this.market, this.piles); 
		
        sender.sendToAllPlayers("Welcome to Point Salad!");
        sender.sendToAllPlayers("There are " + players.size() + " players.");
        
		

         
    }


	public void run(){
		GameLoopSalad game = new GameLoopSalad(this.players,this.piles,this.playerHand,this.market,this.marketPrinter,this.refiller);

		game.runLoop();	
		calculateScorePrint();  
	}

	private void calculateScorePrint(){
		sender.sendToAllPlayers(("\n-------------------------------------- CALCULATING SCORES --------------------------------------\n"));
		for(Player player : players) {
			sender.sendToAllPlayers("Player " + player.getPlayerID() + "'s hand is: \n"+playerHand.displayHand(player.getPlayerID()));
            CalculateScore calculateScore = new CalculateScore(playerHand, player.getPlayerID(), players);
			player.setScore(calculateScore.returnScore());
			sender.sendToAllPlayers("\nPlayer " + player.getPlayerID() + "'s score is: " + player.getScore()+"\n------------------------------------\n\n");
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
				System.out.println("\nThe winner is player " + playerID + " with a score of " + maxScore);
			}
		}
	}
    
       
}

        
        
        
        

        


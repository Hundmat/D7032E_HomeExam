package game.gameLogic;

import game.market.MarketPrinter;
import game.market.RefileMarket;
import game.piles.PileHandler;
import game.players.Player;


import java.util.ArrayList;


public abstract class Game{
    PileHandler piles;
    PileHandler playerHand;
    PileHandler market;
    ArrayList<Player> players;
    MarketPrinter marketPrinter;
    SendToAllPlayers sender;
    RefileMarket refiller;

    public Game(ArrayList<Player> players) {
        this.players = players;
    }


	public void run(){
        //call game
        calculateScorePrint();   

	}

    private void calculateScorePrint(){
        
    };
}

package game.gameLogic;

import game.market.MarketPrinter;
import game.market.RefileMarket;
import game.piles.PileHandler;
import game.players.Player;


import java.util.ArrayList;


public abstract class Game{
    private PileHandler piles;
    private PileHandler playerHand;
    private PileHandler market;
    private ArrayList<Player> players;
    private MarketPrinter marketPrinter;
    private SendToAllPlayers sender;
    private RefileMarket refiller;

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

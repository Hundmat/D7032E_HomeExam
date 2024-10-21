package game.bot;

import game.market.RefileMarketSalad;
import game.piles.PileHandler;
import game.players.Player;
import game.market.RefileMarket; // Added import statement for RefileMarket
import java.util.ArrayList;

public class BotLogic {
    PileHandler piles;
    PileHandler market;
    PileHandler playerHand;
    Player thisPlayer;
    ArrayList<Player> players;
    String output = "";
    RefileMarket refiller;

    public BotLogic(PileHandler piles, PileHandler market, PileHandler playerHand, Player thisPlayer, ArrayList<Player> players, RefileMarket refiller) {
        // TODO Auto-generated constructor stub
        this.piles = piles;
        this.market = market;
        this.playerHand = playerHand;
        this.thisPlayer = thisPlayer;
        this.players = players;
        this.refiller = refiller;
    }

    public void run() {
        //Bot Logic
    }

    public String returnString() {
        return this.output;
    }
    
}

package game.bot;

import game.piles.PileHandler;
import game.players.Player;
import java.util.ArrayList;

public class BotLogic {
    PileHandler piles;
    PileHandler market;
    PileHandler playerHand;
    Player thisPlayer;
    ArrayList<Player> players;
    String output = "";

    public BotLogic(PileHandler piles, PileHandler market, PileHandler playerHand, Player thisPlayer, ArrayList<Player> players) {
        // TODO Auto-generated constructor stub
        this.piles = piles;
        this.market = market;
        this.playerHand = playerHand;
        this.thisPlayer = thisPlayer;
        this.players = players;
    }

    public void run() {
        //Bot Logic
    }

    public String returnString() {
        return this.output;
    }
    
}

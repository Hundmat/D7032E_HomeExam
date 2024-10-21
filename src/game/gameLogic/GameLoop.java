package game.gameLogic;

import game.piles.PileHandler;
import game.players.Player;
import game.bot.BotLogic;
import game.market.MarketPrinterSalad;
import game.market.RefileMarket;
import java.util.ArrayList;

public abstract class GameLoop {
    SendToAllPlayers sender;
    PileHandler piles; 
    PileHandler market; 
    PileHandler playerHand; 
    Player thisPlayer;
    ArrayList<Player> players;
    RefileMarket refiller;

    public GameLoop( ArrayList<Player> players, PileHandler piles,PileHandler playerHand,PileHandler market,MarketPrinterSalad marketPrinter,RefileMarket refiller){
        this.sender = new SendToAllPlayers(players);
        this.piles = piles;
        this.market = market;
        this.playerHand = playerHand;
        this.players = players;
        this.refiller = refiller;
        
        refiller.run();
    };

    public void runLoop(){
        //Loop logic
        if(checkIfContinue()){
            playerAction();
            
            BotLogic bot = new BotLogic(this.piles, this.market, this.playerHand, this.thisPlayer, this.players, this.refiller);
            bot.run();
        }
        
    };

    private void playerAction(){
        //player loop actions
    };

    private boolean checkIfContinue(){
        return true;
    }
}

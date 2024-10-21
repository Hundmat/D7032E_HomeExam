package game.bot;

import game.market.MarketPile;
import game.piles.SetPileSalad;
import game.players.PlayerHand;
import game.score.CalculateScore;
import game.piles.Card;
import game.players.Player;
import java.util.ArrayList;
import game.market.RefileMarketSalad;

public class BotLogicSalad extends BotLogic {
    MarketPile market;
    PlayerHand playerHand;
    RefileMarketSalad refiller;

    public BotLogicSalad(SetPileSalad piles, MarketPile market, PlayerHand playerHand, Player thisPlayer, ArrayList<Player> players, RefileMarketSalad refiller) {
        super(piles, market, playerHand, thisPlayer, players,refiller);
        this.market = market;
        this.playerHand = playerHand;
        this.refiller=refiller;
        
    }

    public void run(){
        // The Bot will randomly decide to take either one point card or two veggie cards 
        // For point card the Bot will always take the point card with the highest score
        // If there are two point cards with the same score, the bot will take the first one
        // For Veggie cards the Bot will pick the first one or two available veggies
        boolean emptyPiles = false;
        int choice = 0;
        // Random choice: 
        if(this.refiller.PilesAreEmpty()!=0){
            choice = (int) (Math.random() * 2);
        }else{
            choice = 1;
        }
        
        if(choice == 0) {
            // Take a point card
            int highestPointCardIndex = 0;
            int highestPointCardScore = 0;
            for(int i = 0; i < piles.getPiles().size(); i++) {
                if(piles.getPile(i).getCard(0) != null) {
                    System.out.println("Checking point card1: "+piles.getPile(i).getCard(0));
                    PlayerHand tmpHand = new PlayerHand(1);
                    for(Card handCard : playerHand.getPile(thisPlayer.getPlayerID()).getAll()) {
                        tmpHand.getPile(0).addCard(handCard);
                    }
                    tmpHand.getPile(0).addCard( playerHand.getPile(thisPlayer.getPlayerID()).getCard(0));
                    CalculateScore calculateScore = new CalculateScore(tmpHand, 0, players);
                    int score = calculateScore.returnScore();
                    if(score > highestPointCardScore) {
                        highestPointCardScore = score;
                        highestPointCardIndex = i;
                    }
                }
            }
            if(piles.getPile(highestPointCardIndex).getCard(0) != null) {
                System.out.println("Buying point card: "+piles.getPile(highestPointCardIndex).getCard(0));
                playerHand.getPile(thisPlayer.getPlayerID()).addCard(piles.getPile(highestPointCardIndex).buyCard(0));
                return;
            } else {
                choice = 1; //buy veggies instead
                emptyPiles = true;
            }
        } else if (choice == 1) {
            // TODO: Check what Veggies are available and run calculateScore to see which veggies are best to pick
            int cardsPicked = 0;
            for(int i = 0; i < market.getPiles().size(); i++) {
                if(market.getCardFromMarket(i,0) != null && cardsPicked < 2) {
                    System.out.println("Buying point card: "+thisPlayer.getPlayerID());

                    playerHand.getPile(thisPlayer.getPlayerID()).addCard(market.buyCard(i,0));
                    cardsPicked++;
                }
                if(market.getCardFromMarket(i,1) != null && cardsPicked < 2) {
                    System.out.println("Buying point card: "+thisPlayer.getPlayerID());

                    playerHand.getPile(thisPlayer.getPlayerID()).addCard(market.buyCard(i,1));
                    cardsPicked++;
                }
            }
            if(cardsPicked == 0 && !emptyPiles) {
                // Take a point card instead of veggies if there are no veggies left
                int highestPointCardIndex = 0;
                int highestPointCardScore = 0;
                for(int i = 0; i < market.getPiles().size(); i++) {
                    if(piles.getPile(i).getCard(0) != null) {
                        System.out.println("Checking point card2: "+piles.getPile(i).getCard(0));
                        PlayerHand tmpHand = new PlayerHand(1);
                        for(Card handCard : playerHand.getPile(thisPlayer.getPlayerID()).getAll()) {
                            tmpHand.getPile(0).addCard(handCard);
                        }
                        tmpHand.getPile(0).addCard( playerHand.getPile(thisPlayer.getPlayerID()).getCard(0));
                        CalculateScore calculateScore = new CalculateScore(tmpHand, 0, players);
                        int score = calculateScore.returnScore();
                        if(score > highestPointCardScore) {
                            highestPointCardScore = score;
                            highestPointCardIndex = i;
                        }
                    }
                }
                if(piles.getPile(highestPointCardIndex).getCard(0) != null) {
                    System.out.println("Buying point card: "+thisPlayer.getPlayerID()+" "+piles.getPile(highestPointCardIndex).getCard(0));

                    playerHand.getPile(thisPlayer.getPlayerID()).addCard(piles.getPile(highestPointCardIndex).buyCard(0));
                    return;
                }
            }
        }
        this.output = "Bot " + thisPlayer.getPlayerID() + "'s hand is now: \n"+playerHand.displayHand(thisPlayer.getPlayerID())+"\n";
    }

    public String returnString() {
        return this.output;
    }
    
}

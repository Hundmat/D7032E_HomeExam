package game.score;

import game.players.Player;
import game.players.PlayerHand;
import java.util.ArrayList;


public abstract class AbstractCalculateScore {
    int score;
    public AbstractCalculateScore(PlayerHand hand, int playerID, ArrayList<Player> players){
        //Score logic
    };

    public int returnScore() {
        return this.score;
    }
    
}

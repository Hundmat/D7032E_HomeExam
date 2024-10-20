package network;

import game.players.Player;
import java.util.ArrayList;

public interface IServer {
    public void runServer(int numberPlayers, int numberOfBots, ArrayList<Player> players ) throws Exception;
}

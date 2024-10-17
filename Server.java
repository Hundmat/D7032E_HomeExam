package network;

import java.util.ArrayList;

import game.players.IPlayer;
import game.players.Player;
import game.players.PlayerBot;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server<T extends Enum<T>> {

    private ServerSocket aSocket;
    private ArrayList<IPlayer> players = new ArrayList<IPlayer>();
    long seed;

    public void sendToAllPlayers(String message) {
		for(IPlayer player : players) {
			player.sendMessage(message);
		}
	}

    public IPlayer getPlayer(int playerID) {
        return players.get(playerID);
    }

    public ArrayList<IPlayer> getPlayers() {
        return players;
    }

    public Server(int numberPlayers, int numberOfBots) throws Exception {

        this.seed = new java.util.Random().nextLong();

        this.players.add(new Player<T>(0, null, null, null,seed)); //add this instance as a player
        //Open for connections if there are online players
        for(int i=0; i<numberOfBots; i++) {
            this.players.add(new PlayerBot<T>(i+1,seed)); //add a bot    
        }
        if(numberPlayers>1)
            aSocket = new ServerSocket(2048);
        for(int i=numberOfBots+1; i<numberPlayers+numberOfBots; i++) {
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            this.players.add(new Player<T>(i, connectionSocket, inFromClient, outToClient,seed)); //add an online client
            System.out.println("Connected to player " + i);
            outToClient.writeObject("You connected to the server as player " + i + "\n");
        }    
    }
    
}
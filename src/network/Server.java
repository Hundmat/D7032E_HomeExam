package network;

import game.players.Player;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Server implements IServer{
    private int numberPlayers;
    private int numberOfBots;
    private ArrayList<Player> players;


    /**
     * Runs the server to accept connections from players and bots.
     *
     * @param numberPlayers The number of human players to connect to the server.
     * @param numberOfBots The number of bot players to include in the game.
     * @param players The list of players (both human and bot) participating in the game.
     * @throws Exception If an error occurs while running the server.
     */
    public void runServer(int numberPlayers, int numberOfBots, ArrayList<Player> players ) throws Exception{
        ServerSocket aSocket = null;
        this.numberPlayers = numberPlayers;
        this.numberOfBots = numberOfBots;
        this.players = players;
        


                
        aSocket = new ServerSocket(2048);
        
        for(int i=this.numberOfBots; i<this.numberPlayers+this.numberOfBots; i++) {
            System.out.println("Waiting for player " + i);
            try {
                Socket connectionSocket = aSocket.accept();
                ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
                ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
                
                this.players.get(i).update(inFromClient, outToClient, connectionSocket, true); //update the player

                System.out.println("Connected to player " + i);
                outToClient.writeObject("You connected to the server as player " + i + "\n");
            } catch (Exception e) {
                System.err.println("Connection lost with player " + i);
                e.printStackTrace();
            }

        }    
    }
}

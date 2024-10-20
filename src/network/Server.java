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


    public void runServer(int numberPlayers, int numberOfBots, ArrayList<Player> players ) throws Exception{
        ServerSocket aSocket = null;
        this.numberPlayers = numberPlayers;
        this.numberOfBots = numberOfBots;
        this.players = players;
        


                
        aSocket = new ServerSocket(2048);
        
        for(int i=this.numberOfBots; i<this.numberPlayers+this.numberOfBots; i++) {
            System.out.println("Waiting for player " + i);
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            

            this.players.get(i).update(inFromClient, outToClient, connectionSocket, true); //update the player

            System.out.println("Connected to player " + i);
            outToClient.writeObject("You connected to the server as player " + i + "\n");

        }    
    }
}

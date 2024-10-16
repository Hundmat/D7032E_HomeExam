package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import players.Player;
import java.util.ArrayList;

public class Server {

    private ServerSocket aSocket;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private Socket connectionSocket;
    
    private int numberPlayers;
    private int numberOfBots;

    public void Server(int numberPlayers, int numberOfBots) throws Exception {
        this.numberPlayers = numberPlayers;
        this.numberOfBots = numberOfBots;
        

    }
    
    public void holder(){
        //Open for connections if there are online players
        for(int i=0; i<numberOfBots; i++) {
            players.add(new Player(i+1, true, null, null, null)); //add a bot    
        }
        if(numberPlayers>1)
            aSocket = new ServerSocket(2048);
        for(int i=this.numberOfBots+1; i<this.numberPlayers+this.numberOfBots; i++) {
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());

            System.out.println("Connected to player " + i);
            outToClient.writeObject("You connected to the server as player " + i + "\n");
        }  
    }
}
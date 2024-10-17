package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;

public class ServerTest implements MessageHandler {

    
   
    private ArrayList<ObjectOutputStream> outputStreams = new ArrayList<>();
    private ArrayList<Integer> playerIDs = new ArrayList<>();
    private ObjectInputStream inFromClient;
    private long seed;
    private int numberPlayers;
    private int numberOfBots;
    private ServerSocket aSocket;
    private int startPlayerID;


    public ServerTest(int numberPlayers, int numberOfBots) throws Exception {
        this.seed = new java.util.Random().nextLong();
        this.numberPlayers = numberPlayers;
        this.numberOfBots = numberOfBots;
        
        for(int i=0; i<this.numberPlayers+this.numberOfBots; i++){
            this.playerIDs.add(i);
        }
        startPlayerID = new java.util.Random().nextInt(this.numberPlayers);
        
        this.aSocket = new ServerSocket(2048);
        System.out.println("Server is running on port 2048");
        for(int i=0; i<this.numberPlayers; i++) {
            Socket connectionSocket = aSocket.accept();
            this.inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            outputStreams.add(outToClient);
            messageCreator(i);            
        }

        for(int i=this.numberPlayers; i<this.numberPlayers+this.numberOfBots; i++) {
            Socket connectionSocket = aSocket.accept();
            this.inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            outputStreams.add(outToClient);
            JSONObject json = new JSONObject();
            json.put("playerID", String.valueOf(i));   // Assuming string.i is a valid value
            json.put("isBot", "true");       // Explicitly setting as a string "true"
            json.put("seed", String.valueOf(seed));    // Assuming string.seed is a valid value
            sendMessageToPlayer(outToClient,json.toString());     // Converts the JSON object to a string and sends it
        }
        
        listenForClientMessages();
    }


    private void messageCreator (int i) {
        JSONObject json = new JSONObject();
        if (i == startPlayerID) {
            json.put("isStartPlayer", "true");
        } else {
            json.put("isStartPlayer", "false");
        }
        json.put("playerID", String.valueOf(i));   // Assuming string.i is a valid value
        json.put("isBot", "false");       // Explicitly setting as a string "false"
        json.put("seed", String.valueOf(this.seed));    // Assuming string.seed is a valid value
        json.put("amountofplayers", String.valueOf(this.numberPlayers+this.numberOfBots));    // Assuming string.seed is a valid value
        sendMessageToPlayer(this.outputStreams.get(i),json.toString());     // Converts the JSON object to a string and sends it
        System.out.println("Connected to player " + i);
    }

    public void sendMessage(String message) {

        for (ObjectOutputStream outputStream : outputStreams) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Error sending message to client: " + e.getMessage());
            }
        }
        
    }

    public void sendMessageToPlayer(ObjectOutputStream outToClient, String message) {
        try {
            outToClient.writeObject(message);
            outToClient.flush();
        } catch (IOException e) {
            System.out.println("Error sending message to client: " + e.getMessage());
        }
    }
    public String receiveMessage() {
        try {
            return (String) this.inFromClient.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error receiving message from client: " + e.getMessage());
        }
        return null;
    }

    public void listenForClientMessages() {
        try {
            while (true) {
                String clientMessage = receiveMessage();
                if (clientMessage != null) {
                    System.out.println("Received message from client: " + clientMessage);
                    // Handle the client message (e.g., process player action)
                    processClientMessage(clientMessage);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while listening for client messages: " + e.getMessage());
        }
    }

    private void processClientMessage(String message) {
        // Here we would process the client message, like handling game moves or actions.
        System.out.println("Processing client message: " + message);

        // Optionally, we can send a response back to the client
        sendMessage("Server received: " + message);
    }
}


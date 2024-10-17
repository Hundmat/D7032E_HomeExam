package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements MessageHandler {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private MessageListener listener;

 

    public Client(String ipAddress,MessageListener listener) throws Exception {
        //Connect to server
        this.listener = listener;

        Socket aSocket = new Socket(ipAddress, 2048);
        this.outputStream = new ObjectOutputStream(aSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(aSocket.getInputStream());
        String nextMessage = "";
        listenForUpdates();
    }
    
    public void sendMessage(String message) {
        try {
            System.out.println("Sending message to server: " + message);
            this.outputStream.writeObject(message);
            this.outputStream.flush();
        } catch (IOException e) {
            System.out.println("Error sending message to server: " + e.getMessage());
        }
    }

    @Override
    public String receiveMessage() {
        try {
            return (String) this.inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error receiving message from server: " + e.getMessage());
        }
        return null;
    }

    public void listenForUpdates() {
        try {
            while (true) {
                String updateMessage = receiveMessage();
                if (updateMessage != null) {
                    this.listener.onUpdate(updateMessage); // Update the listener with server message
                }
            }
        } catch (Exception e) {
            System.out.println("Error while listening for updates: " + e.getMessage());
        }
    }

    // A method to send player's action back to the server
    public void sendPlayerAction(String actionMessage) {
        sendMessage(actionMessage); // Send the player's action to the server
    }
}
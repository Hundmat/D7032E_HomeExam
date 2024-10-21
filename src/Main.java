import game.bot.PlayerBot;
import game.gameLogic.Game;
import game.players.HumanPlayer;
import game.players.Player;
import network.IClient;
import network.IServer;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Main {
    private IServer server;
    IClient client;
    private int numberPlayers = 0;
    private int numberOfBots = 0;
    private ArrayList<Player> players = new ArrayList<>();

    // Method to start the server
    private void serverStart() {
        Scanner in = new Scanner(System.in);
 
        try {
            System.out.println("Please enter the number of players");
            numberPlayers = in.nextInt();

            System.out.println("Please enter the number of bots");
            numberOfBots = in.nextInt();

            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the scanner to avoid resource leaks
            in.close();
        }
        for (int i = 0; i < numberOfBots; i++) {
            players.add(new PlayerBot(i,null, null, null,false)); //add this instance as a player
        }
        for (int i = numberOfBots; i < numberPlayers+numberOfBots; i++) {
            players.add(new HumanPlayer(i,null, null, null,false)); //add this instance as a player
        }

        System.out.println(players);
        try {
            System.out.println("Server started");
            
            server.runServer(numberPlayers, numberOfBots, players);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Start game instant
    }

    private void clientStart(String host) {
        try {
            client.runClient(host);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // The main method must be static
    public static void main(String[] args) {
        /**
         * Creates an instance of the Main class.
         */
        Main instance = new Main() {}; // Create an anonymous subclass instance of the abstract class
        System.out.println(args[0]);
        if (args.length > 0 && args[0].equals("server")) { // Check if argument is 'server'
            instance.serverStart(); // Call the server start method
        } else {
            instance.clientStart("127.0.0.1"); // Call the client start method
        }
    }
}

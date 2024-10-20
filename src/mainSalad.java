import java.util.Scanner;
import java.util.ArrayList;
import network.Server;
import game.players.Player;
import game.bot.PlayerBot;
import game.players.HumanPlayer;
import network.Client;

public class mainSalad {
    private Server server;
    private int numberPlayers = 0;
    private int numberOfBots = 0;
    private ArrayList<Player> players = new ArrayList<>();

    // Method to start the server
    private void serverStart() {
        Scanner in = new Scanner(System.in);
 
        try {
            System.out.println("Please enter the number of players (1-6): ");
            numberPlayers = in.nextInt();

            System.out.println("Please enter the number of bots (0-5): ");
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
            server = new Server(numberPlayers, numberOfBots, players);
        } catch (Exception e) {
            e.printStackTrace();
        }

        game.PointSalad game = new game.PointSalad(players);
    }

    private void clientStart(String host) {
        try {
            Client client = new Client(host);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // The main method must be static
    public static void main(String[] args) {
        mainSalad instance = new mainSalad(); // Create an instance of the class
        System.out.println(args[0]);
        if (args.length > 0 && args[0].equals("server")) { // Check if argument is 'server'
            instance.serverStart(); // Call the server start method
        } else {
            instance.clientStart("127.0.0.1"); // Call the client start method
        }
    }
       
}


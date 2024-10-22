import java.util.Scanner;
import java.util.ArrayList;
import network.Server;
import game.players.Player;
import game.bot.PlayerBot;
import game.players.HumanPlayer;
import network.Client;
import game.gameLogic.PointSalad;

public class MainSalad extends Main {
    private Server server;
    private int numberPlayers = 0;
    private int numberOfBots = 0;
    private ArrayList<Player> players = new ArrayList<>();

    // Method to start the server
    private void serverStart() {
        Scanner in = new Scanner(System.in);
        while (true){
            try {
                System.out.println("Please enter the number of players (1-6): ");
                numberPlayers = in.nextInt();
    
                System.out.println("Please enter the number of bots (0-6): ");
                numberOfBots = in.nextInt();
    
                
            } catch (Exception e) {
                e.printStackTrace();
            } 

            if(numberOfBots+numberPlayers<2){
                System.out.println("You need to be two players atleast to play");
            }else if(numberOfBots+numberPlayers>6){
                System.out.println("You need to be less than six players to play");
            }else{
                in.close();
                break;
            }

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
            server = new Server();
            server.runServer(numberPlayers, numberOfBots, players);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PointSalad game = new PointSalad(players);

        
        game.run();
    }

    private void clientStart(String host) {
        try {
            Client client = new Client();
            client.runClient(host);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // The main method must be static
    public static void main(String[] args) {
        MainSalad instance = new MainSalad(); // Create an instance of the class
        System.out.println(args[0]);
        if (args.length > 0 && args[0].equals("server")) { // Check if argument is 'server'
            instance.serverStart(); // Call the server start method
        } else {
            instance.clientStart(args[0]); // Call the client start method
        }
    }
       
}


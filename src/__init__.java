import java.util.Scanner;

import network.Client;
import network.ServerTest;

public class __init__ {
    private static ServerTest server;
    public static void main(String[] args) {
        int numberPlayers = 0;
        int numberOfBots = 0;
        
        
        System.out.println("Please enter the number of players (1-6): ");
        Scanner in = new Scanner(System.in);
        numberPlayers = in.nextInt();
        System.out.println("Please enter the number of bots (0-5): ");
        numberOfBots = in.nextInt();
        try {
            server = new ServerTest(numberPlayers, numberOfBots);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(true) {
            
        }
    }

  
}

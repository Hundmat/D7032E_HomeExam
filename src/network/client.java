package network;

import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Scanner;

public class Client{
    public Client(String ipAddress) throws Exception {
        //Connect to server
        Socket aSocket = new Socket(ipAddress, 2048);
        ObjectOutputStream outToServer = new ObjectOutputStream(aSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(aSocket.getInputStream());
        String nextMessage = "";
        while(!nextMessage.contains("winner")){
            nextMessage = (String) inFromServer.readObject();
            System.out.println(nextMessage);
            if(nextMessage.contains("Take") || nextMessage.contains("into")) {
                Scanner in = new Scanner(System.in);
                outToServer.writeObject(in.nextLine());
                outToServer.flush();
            }
        }
        aSocket.close();
    }
}
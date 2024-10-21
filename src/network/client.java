package network;

import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Scanner;

public class Client implements IClient{
    

    /**
     * Connects to a server at the specified IP address and port 2048, then continuously
     * listens for messages from the server. If a message contains "Take" or "into",
     * it prompts the user for input and sends the input back to the server. The loop
     * terminates when a message containing "winner" is received.
     *
     * @param ipAddress the IP address of the server to connect to
     * @throws Exception if an I/O error occurs when creating the socket or streams,
     *                   or if an error occurs during communication with the server
     */
    public void runClient(String ipAddress) throws Exception {
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
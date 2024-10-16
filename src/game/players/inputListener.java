package game;

public class inputListener {
    public String readMessage() {
        String word = ""; 
        if(online)
            try{word = (String) inFromClient.readObject();} catch (Exception e){}
        else
            try {word=in.nextLine();} catch(Exception e){}
        return word;
    }
}

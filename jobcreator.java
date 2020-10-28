import java.io.*;
import java.net.*;
//java server 
public class jobcreator{
    
    public static void main(String[] args) {
        String hostname = "127.0.0.1"; // to be changed later
        int port = 80;

       try{
            Socket socket = new Socket(hostname, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket.close();
       }

       catch(Exception e){
           System.out.println(e);
       }

        
    }
}
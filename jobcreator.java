import java.io.*;
import java.net.*;
//java server 
public class jobcreator{
    
    public static void main(String[] args) {
        int port = 80;

       try{
            ServerSocket serversocket = new ServerSocket(port);
            while (true) {
                Socket socket = serversocket.accept();
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = reader.readLine();
                // Sending data
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println("Job Creator Message");
                socket.close();
                serversocket.close();
            }  
       }

       catch(Exception e){
           System.out.println(e);
       }

        
    }
}
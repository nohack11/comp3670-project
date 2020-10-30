import java.io.*;
import java.net.*;
import java.util.Scanner;

//java server 
public class jobcreator {

    public static void main(String[] args) {
        int port = 80;

        try {
            ServerSocket serversocket = new ServerSocket(port);
            Scanner sc = new Scanner(System.in);
            while (true) {
                Socket socket = serversocket.accept();
                String client = socket.getInetAddress().getHostAddress();

                // Receiving data
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                System.out.println("Job Creator connected");
                System.out.println("Client address is: "+client);
                String line;

                while(true){

                    while((line = reader.readLine()) != null){
                        System.out.println("Client Message: "+line);
                    }
                                    // Sending data
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    
                    writer.println("Job Creator Message");

                // 
                    socket.close();
                    serversocket.close();
                }
                
                
            }  
       }

       catch(Exception e){
           System.out.println(e);
       }

        
    }
}
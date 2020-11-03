// A Java program for a Server
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

//java server 
public class jobcreator {

    public static void main(String[] args) {
        int port = 5000;

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
                System.out.println("======== MESSAGE RECEIVED ========");
                String line = reader.readLine();
                //System.out.println("");
                System.out.println("Message from Jobseeker: "+ line);
                
                    System.out.println("======== SENDING DATA ===========");

                                    // Sending data
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    
                    writer.println("JobCreator Message");
                    System.out.println("Message sent ...");
                    writer.flush();
                    System.out.println("Jobcreator waiting... ^C to terminate");
                //TimeUnit.SECONDS.sleep(5);
                  socket.close();

            } 
       }

       catch(Exception e){
           System.out.println(e);
       }

    public static void main(String args[])
    {
        jobcreator server = new jobcreator(80);
    }
}


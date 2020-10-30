import java.io.*;
import java.net.*;
import java.util.Scanner;

// client 
//Ariya is working on it
public class jobseeker{



    public static void main(String args[]){

        Scanner sc = new Scanner(System.in);
        // initialize socket and input output streams
        Socket socket            = null;
        //DataInputStream  input   = null;
        DataOutputStream out     = null;
        // constructor to put ip address and port


        String hostname = "127.0.0.1"; // to be changed later
        int port = 80;

        InputStream input = null;
        try {
            socket = new Socket(hostname, port);
            System.out.println("Connected");

            // takes input from terminal
            //input = new DataInputStream(System.in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
        }

        catch(UnknownHostException u) {
                System.out.println(u);
        }

        catch(IOException i) {
                System.out.println(i);
        }

        String line = "";
// keep reading until "Over" is input -- Reading from the input
        while (!line.equals("Over"))
        {
            
            
                line =sc.nextLine();
                System.out.println(line);
            

            //catch(IOException i)
            //{
            //    System.out.println(i);
            //}
        }
//------------------------------------------------------------------------

//closing the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
//------------------------------------------------------------------------------------
    }

}

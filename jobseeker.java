import java.io.*;
import java.net.*;

// client 
//Ariya is working on it
public class jobseeker{



    public static void main(String args[]){

        // initialize socket and input output streams
        Socket socket            = null;
        DataInputStream  input   = null;
        DataOutputStream out     = null;
        // constructor to put ip address and port


        String hostname = "127.0.0.1"; // to be changed later
        //URL url; // we could also use a URL
        int port = 80;

        try {
            socket = new Socket(hostname, port);
            System.out.println("Connected");

            // takes input from terminal
            input = new DataInputStream(System.in);

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
            try
            {
                line = input.readLine();
                out.writeUTF(line);
            }

            catch(IOException i)
            {
                System.out.println(i);
            }
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

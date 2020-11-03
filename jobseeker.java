// A Java program for a Client
import java.net.*;
<<<<<<< HEAD
import java.util.Scanner;

// client
//Ariya is working on it
public class jobseeker{



    public static void main(String args[]) throws IOException {

        Scanner sc = new Scanner(System.in);
        // initialize socket and input output streams
        Socket socket            = null;
        //DataInputStream  input   = null;
        //DataOutputStream out     = null;
        // constructor to put ip address and port


        String hostname = "127.0.0.1"; // to be changed later
        int port = 5000;

        InputStream input = null;
       
        try {
            socket = new Socket(hostname, port);
=======
import java.io.*;

public class jobseeker
{
    // initialize socket and input output streams
    private Socket socket		 = null;
    private DataInputStream input = null;
    private DataOutputStream out	 = null;

    // constructor to put ip address and port
    public jobseeker(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
>>>>>>> c6d2ac405ce0fb2b9afed081cb9178fed297166b
            System.out.println("Connected");

            //sending first
            OutputStream out = socket.getOutputStream();
            PrintWriter toserver = new PrintWriter(out, true);
            System.out.println("======== SENDING DATA  ===========");
            toserver.println("JobSeeker Message");
            System.out.println("Message sent ...");
            // takes input from terminal
<<<<<<< HEAD
            input = socket.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String line;
            line = reader.readLine();
            System.out.println("======== MESSAGE RECEIVED ========");
            System.out.println("From JobCreator:  "+line+"\n");

            System.out.println("Jobseeker closed");
            toserver.flush();
        }

        catch(Exception e) {
            System.out.println(e);
=======
            input = new DataInputStream(System.in);

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
>>>>>>> c6d2ac405ce0fb2b9afed081cb9178fed297166b
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
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

        // close the connection
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
    }

    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 80);
    }
}


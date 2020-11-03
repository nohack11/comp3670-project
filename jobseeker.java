// A Java program for a Client
import java.net.*;
import java.io.*;
import java.util.Scanner;

// client
//Ariya is working on it
public class Jobseeker{



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
            System.out.println("Connected");

            //sending first
            OutputStream out = socket.getOutputStream();
            PrintWriter toserver = new PrintWriter(out, true);
            System.out.println("======== SENDING DATA  ===========");
            toserver.println("JobSeeker Message");
            System.out.println("Message sent ...");
            // takes input from terminal
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
        }
    }

    
}


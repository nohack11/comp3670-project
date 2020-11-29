// A Java program for a Client

import java.io.*;
import java.net.Socket;

public class Jobseeker{

    public static void main(String[] args) {

        String hostname = "127.0.0.1"; // current IP to be changed later
        int port = 5000; // for peer to peer connection change to port = 61555

        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to Jobcreator.");

            // Jobseeker sending Data to a connected Jobcreator
            OutputStream out = socket.getOutputStream();
            PrintWriter toServer = new PrintWriter(out, true);
            System.out.println("========= SENDING DATA ===========");
            toServer.println("JobSeeker Message"); // Message sent to Jobcreator
            System.out.println("Message sent ...");

            // Jobseeker receiving Data from a connected Jobcreator
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine(); // data received
            System.out.println("======== MESSAGE RECEIVED ========");
            System.out.println("From JobCreator:  " + line + "\n");

            // JOB ASSIGNMENTS
            System.out.println("Waiting for job assignments...");
            String job = reader.readLine();
            System.out.println("Job Assignments:");
            System.out.println("JOB: " + job);
            toServer.println("done");
            System.out.println("Jobseeker closed.");
            toServer.flush();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    
}


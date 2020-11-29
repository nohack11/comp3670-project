// A Java program for a Client

import java.io.*;
import java.net.InetAddress;
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
            String[] tokens = job.split(","); // Contains data of job
            switch(Integer.parseInt(tokens[0])) {
                // JOB: Detect if a given IP address or Host Name is online or not
                case 1:
                    boolean isOnline;
                    // Detecting by IP
                    if(Integer.parseInt(tokens[1]) == 1) {
                        // Splitting user entered IP address
                        String newToken = tokens[2].replace('.', ',');
                        String[] ipString = newToken.split(",");

                        // Converting string IP address into byte array
                        byte[] ipAddress = new byte[ipString.length];
                        for(int i = 0; i < ipString.length; i++) {
                            int part = Integer.parseInt(ipString[i]);
                            ipAddress[i] = (byte) part;
                        }

                        // Checking if IP address is online
                        isOnline = InetAddress.getByAddress(ipAddress).isReachable(5000);
                    }
                    // Detecting by Host Name
                    else
                        isOnline = InetAddress.getByName(tokens[2]).isReachable(5000); // Checking if Host Name is online

                    // Output of job
                    if(isOnline)
                        toServer.println(tokens[2] + " is online.");
                    else
                        toServer.println(tokens[2] + " is not online.");
                    return;
                case 2:
                    return;
                case 3:
            }
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


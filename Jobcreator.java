// A Java program for a Server

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

// This jobcreator Prints out a message to notify connection with jobseeker
// JobCreator then prints the IP address of JobSeeker

public class Jobcreator {
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int port = 5000; // for peer to peer connection change to port = 61555

        try {
            ServerSocket serversocket = new ServerSocket(port);

            while(true) {
                Socket socket = serversocket.accept();
                String client = socket.getInetAddress().getHostAddress();
                // Receiving data
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                System.out.println("Job Creator connected.");
                System.out.println("Client address is: " + client);

                //Jobcreator receiving Data from a connected Jobseeker
                System.out.println("======== MESSAGE RECEIVED ========");
                String line = reader.readLine();
                System.out.println("Message from Jobseeker: " + line);

                //JobCreator Sending Data to a connected Jobseeker
                System.out.println("========= SENDING DATA ===========");
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println("JobCreator Message"); // the message being sent to jobseeker
                System.out.println("Message sent...");

                switch(jobOptions()) {
                    // JOB: Detect if a given IP address or Host Name is online or not
                    case 1:
                        System.out.println("1. Detect by IP address.\n2. Detect by host name.");
                        int mode = sc.nextInt();
                        if(mode == 1)
                            System.out.print("Please enter the IP address: ");
                        else
                            System.out.print("Please enter the host name: ");
                        sc.nextLine();
                        String who = sc.nextLine();

                        // Forward job information to Jobseeker
                        writer.printf("1,%d,%s\n", mode, who);

                        // Retrieve and output job result from Jobseeker
                        String result = reader.readLine();
                        System.out.println(result);
                        return;
                    case 2:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }

                // JOB ASSIGNMENTS
                writer.println("Perform your first job\nPrint Job one");
                String completion = reader.readLine();

                if(completion.contains("done")){
                    System.out.println("Job Completed");
                    break;// when job is complete break
                }
                writer.flush();
                System.out.println("Jobcreator waiting... ^C to terminate");
                socket.close();
                sc.close();
            }
       }
       catch(Exception e){
           e.printStackTrace();
       }
    }

    public static int jobOptions() {
        System.out.println("What job would you like Jobseeker to perform?");
        System.out.println("1. Detect if a given IP address or Host Name is online or not.");

        return sc.nextInt();
    }
}


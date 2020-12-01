// A Java program for a Server

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
//import java.util.concurrent.ExecutorService;

// This jobcreator Prints out a message to notify connection with jobseeker
// JobCreator then prints the IP address of JobSeeker

public class Jobcreator {
    public static Scanner sc = new Scanner(System.in);
    public static Thread thread  = new Thread();
    public static Socket socket = null;
    public static ServerSocket serversocket = null;
    public static void main(String[] args) {
        int port = 5000; // for peer to peer connection change to port = 61555
        try {
            serversocket = new ServerSocket(port);

            while(true) {

                run();

                String client = socket.getInetAddress().getHostAddress();

                // Opening input stream with Jobseeker
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                System.out.println("Connected to Jobseeker.");
                System.out.println("Client address is: " + client);

                // Opening output stream with Jobseeker
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                // Give jobs to Jobseeker until Jobcreator no longer wants to
                boolean stayConnected = true;
                while(stayConnected) {
                    String result;
                    switch (jobOptions()) {
                        // JOB: Detect if a given IP address or Host Name is online or not
                        case 1:
                            System.out.println("1. Detect by IP address.\n2. Detect by host name.");
                            int mode = sc.nextInt();
                            if (mode == 1)
                                System.out.print("Please enter the IP address: ");
                            else
                                System.out.print("Please enter the host name: ");
                            sc.nextLine();
                            String who = sc.nextLine();

                            // Forward job information to Jobseeker
                            writer.printf("1,%d,%s\n", mode, who);

                            // Retrieve and output job result from Jobseeker
                            result = reader.readLine();
                            if (result.compareTo("error") == 0)
                                System.out.println("Jobseeker: Error in input data. Cannot complete job.");
                            else
                                System.out.println(result);
                            break;
                        // JOB: Detect the status of a given port at a given IP address
                        case 2:
                            System.out.print("Please enter the IP address: ");
                            sc.nextLine();
                            String ip = sc.nextLine();
                            System.out.print("Please enter the port number: ");
                            String portNum = sc.nextLine();

                            writer.printf("2,%s,%s\n", ip, portNum);

                            result = reader.readLine();
                            if (result.compareTo("error") == 0)
                                System.out.println("Jobseeker: Error in input data. Cannot complete job.");
                            else
                                System.out.printf("The status of port %s at IP address %s is: %s\n", portNum, ip, result);
                            break;
                        // The Jobcreator wishes to disconnect from the Jobseeker
                        case 3:
                            writer.println("3,");
                            stayConnected = false;
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                }

                writer.flush();
                socket.close();
                // See if Jobcreator wants to find another Jobseeker or stop creating jobs
                System.out.println("1. Wait for another Jobseeker.\n2. Stop waiting and exit.");
                int answer;
                do {
                    answer = sc.nextInt();
                    if(answer < 1 || answer > 2)
                        System.out.println("Please enter a valid option number.");
                    else
                        break;
                } while(true);

                if(answer == 1) {
                    System.out.println("Waiting for Jobseeker...");
                }
                else
                    break;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        thread.interrupt();
        sc.close();
    }
    public static void run(){
        try {
            socket = serversocket.accept();
            if(thread.isInterrupted())
                thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int jobOptions() {
        System.out.println("What job would you like Jobseeker to perform?");
        System.out.println("1. Detect if a given IP address or Host Name is online or not.");
        System.out.println("2. Detect the status of a given port at a given IP address.");
        System.out.println("3. Disconnect from Jobseeker.\n");

        return sc.nextInt();
    }
}


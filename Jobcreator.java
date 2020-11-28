
// A Java program for a Server

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;



// This jobcreator Prints out a message to notify connection with jobseeker
// JobCreator then prints the IP address of JobSeeker

public class Jobcreator {

    public static void main(String[] args) {
        int port = 5000;// for peer to peer connection change to port = 61555

        try {
            ServerSocket serversocket = new ServerSocket(port);
            Scanner sc = new Scanner(System.in);

            while (true){
                Socket socket = serversocket.accept();
                String client = socket.getInetAddress().getHostAddress();
                    // Receiving data
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                System.out.println("Job Creator connected");
                System.out.println("Client address is: "+client);

                //Jobcreator receiving Data from a connected Jobseeker
                System.out.println("======== MESSAGE RECEIVED ========");
                String line = reader.readLine();
                System.out.println("Message from Jobseeker: "+ line);

                //JobCreator Sending Data to a connected Jobseeker
                System.out.println("======== SENDING DATA ===========");
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println("JobCreator Message");// the message being sent to jobseeker
                System.out.println("Message sent ...");

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
}


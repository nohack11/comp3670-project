import java.io.*;
import java.net.*;
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

        String line = "";
// keep reading until "Over" is input -- Reading from the input
        // while (!line.equals("Over"))
        // {


        //         line =sc.nextLine();
        //         System.out.println(line);


        //     //catch(IOException i)
        //     //{
        //     //    System.out.println(i);
        //     //}
        // }
        InputStream fromServer = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(fromServer));
        while((line = reader.readLine()) != null){
            System.out.println("Client Message: "+line);
        }
//------------------------------------------------------------------------

//closing the connection
        try
        {
            //input.close();
            //out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
//------------------------------------------------------------------------------------
    }

}



// import java.io.IOException;
// import java.io.PrintWriter;
// import java.net.InetAddress;
// import java.net.Socket;
// import java.util.Scanner;

// public class jobseeker {

//     private Socket socket;
//     private Scanner scanner;

//     private jobseeker(InetAddress serverAddress, int serverPort) throws Exception {
//         this.socket = new Socket(serverAddress, serverPort);
//         this.scanner = new Scanner(System.in);
//     }

//     private void start() throws IOException {
//         String input;

//         while (true) {
//             input = scanner.nextLine();

//             PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
//             out.println(input);
//             out.flush();
//         }
//     }

//     public static void main(String[] args) throws Exception {
//         jobseeker client = new jobseeker(
//                 InetAddress.getByName(args[0]),
//                 Integer.parseInt(args[1]));

//         System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
//         client.start();
//     }
// }

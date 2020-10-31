import java.io.*;
import java.net.*;
import java.util.Scanner;

//java server 
public class jobcreator {

    public static void main(String[] args) {
        int port = 80;

        try {
            ServerSocket serversocket = new ServerSocket(port);
            Scanner sc = new Scanner(System.in);
            while (true) {
                Socket socket = serversocket.accept();
                String client = socket.getInetAddress().getHostAddress();

                // Receiving data
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                System.out.println("Job Creator connected");
                System.out.println("Client address is: "+client);
                String line;

                while(true){

                    while((line = reader.readLine()) != null){
                        System.out.println("Client Message: "+line);
                    }
                                    // Sending data
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    
                    writer.println("JobCreator Message");

                // 
                    socket.close();
                    serversocket.close();
                }
                
                
            }  
       }

       catch(Exception e){
           System.out.println(e);
       }

        
    }
}


// // A Java program for a Server 
// import java.net.*; 
// import java.io.*; 

// public class jobcreator 
// { 
// 	//initialize socket and input stream 
// 	private Socket		 socket = null; 
// 	private ServerSocket server = null; 
// 	private DataInputStream in	 = null; 

// 	// constructor with port 
// 	public jobcreator(int port) 
// 	{ 
// 		// starts server and waits for a connection 
// 		try
// 		{ 
// 			server = new ServerSocket(port); 
// 			System.out.println("Server started"); 

// 			System.out.println("Waiting for a client ..."); 

// 			socket = server.accept(); 
// 			System.out.println("Client accepted"); 

// 			// takes input from the client socket 
// 			in = new DataInputStream( 
// 				new BufferedInputStream(socket.getInputStream())); 

// 			String line = ""; 

// 			// reads message from client until "Over" is sent 
// 			while (!line.equals("Over")) 
// 			{ 
// 				try
// 				{ 
// 					line = in.readUTF(); 
// 					System.out.println(line); 

// 				} 
// 				catch(IOException i) 
// 				{ 
// 					System.out.println(i); 
// 				} 
// 			} 
// 			System.out.println("Closing connection"); 

// 			// close connection 
// 			socket.close(); 
// 			in.close(); 
// 		} 
// 		catch(IOException i) 
// 		{ 
// 			System.out.println(i); 
// 		} 
// 	} 

// 	public static void main(String args[]) 
// 	{ 
// 		jobcreator server = new jobcreator(80); 
// 	} 
// } 


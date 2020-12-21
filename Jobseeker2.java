import java.io.*;
import java.net.*;

public class Jobseeker2 {
    public static Socket socket;
    public static InputStream input;
    public static BufferedReader reader;
    public static OutputStream output;
    public static PrintWriter writer;
    public static boolean unknownHost;

    public static void getIOStreams() {
        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();
        } catch (IOException ignored) { }

        reader = new BufferedReader(new InputStreamReader(input));
        writer = new PrintWriter(output, true);
    }

    public static void closeIOStreams() {
        try {
            input.close();
            output.close();
        } catch (IOException ignored) { }
    }

    public static boolean setSocket(String hostname, int port) {
        try {
            socket = new Socket(hostname, port);
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    public static void closeSocket() {
        try {
            socket.close();
        } catch(IOException ignored) { }
    }

    public static byte[] convertIP(String IPString) {
        String replaced = IPString.replace('.', ',');
        String[] newIPString = replaced.split(",");

        byte[] ipAddress = new byte[newIPString.length];
        for(int i = 0; i < newIPString.length; i++) {
            ipAddress[i] = (byte) Integer.parseInt(newIPString[i]);
        }

        return ipAddress;
    }

    public static String getJob() {
        try {
            return reader.readLine();
        }
        catch(IOException e) {
            return "error";
        }
    }

    public static boolean work() {
        String job = getJob();

        // No job received, no need to work
        if(job.compareTo("error") == 0)
            return false;

        // Parse job
        String[] tokens = job.split(",");

        switch (Integer.parseInt(tokens[0])) {
            // JOB: Detect if a given IP address or Host Name is online or not
            case 1:
                // Output of job
                if(isOnline(Integer.parseInt(tokens[1]), tokens[2]))
                    writer.println(tokens[2] + " is online.");
                else {
                    if(unknownHost)
                        writer.println(tokens[2] + " is an unknown host.");
                    else
                        writer.println(tokens[2] + " is not online.");
                    unknownHost = false;
                }
                break;
            // JOB: Detect the status of a given port at a given IP address
            case 2:
                // Output of job
                String output = TCPUDPOpenClose(Integer.parseInt(tokens[1]), tokens[2]);
                if(unknownHost)
                        writer.println(tokens[2] + " is an unknown host.");
                else
                    writer.println("The status of port " + tokens[1] + " at IP address " + tokens[2] + " is: " + output);
                unknownHost = false;
                break;
            default:
                writer.println("Other job output.");
                break;
        }
        return true;
    }

    public static boolean isOnline(int mode, String who) {
        if(mode == 1) {
            try {
                return InetAddress.getByAddress(convertIP(who)).isReachable(2000);
            } catch(UnknownHostException e) {
                unknownHost = true;
                return false;
            } catch(IOException ignored) { }
        }

        else {
            try {
                return InetAddress.getByName(who).isReachable(2000); // Checking if Host Name is online
            } catch(UnknownHostException e) {
                unknownHost = true;
                return false;
            } catch(IOException ignored) { }
        }

        return false;
    }

    public static String TCPUDPOpenClose(int portNum, String ip) {
        String output = "closed";
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByAddress(convertIP(ip));
        } catch (UnknownHostException e) {
            unknownHost = true;
            return "";
        }
        try {
            (new DatagramSocket(portNum, ipAddress)).close();
            output = "UDP open";
            (new Socket(ipAddress, portNum)).close();
            return "TCP and UDP open";
        } catch(IOException ignored) { }

        try {
            (new Socket(ipAddress, portNum)).close();
            output = "TCP open";
        } catch(IOException ignored) { }

        return output;
    }

    public static void main(String[] args) {
        int port = 5000;
        String hostname = "localhost";
        while(true) {
            if(!setSocket(hostname, port))
                break;
            getIOStreams();

            if(!work()) {
                writer.println("error");
                break;
            }

            closeIOStreams();
            closeSocket();
        }

        closeIOStreams();
        closeSocket();
    }
}

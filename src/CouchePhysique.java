import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CouchePhysique {
    private CoucheLiaisonClient liaison;
    private  DatagramSocket socketReception;
    private DatagramSocket socketEnvoi;
    private boolean paquetMalEnvoye;

    public CouchePhysique(){
        try{
            socketReception = new DatagramSocket(25501);
            socketEnvoi = new DatagramSocket();
            paquetMalEnvoye = false;
        }catch (Exception e){
            e.printStackTrace();
            socketReception.close();
            socketEnvoi.close();
        }
    }

    public void lierCoucheLiaison(CoucheLiaisonClient liaison){
        this.liaison = liaison;
    }

    /**
     * Active la booléenne
     */
    public void paquetMalEnvoye(){
        paquetMalEnvoye = true;
    }

    public void EnvoiServeur(String paquet, String adresse){
        try{
            InetAddress address = InetAddress.getByName(adresse);

            if (paquetMalEnvoye && Integer.parseInt(paquet.substring(0,4)) == 1 && Integer.parseInt(paquet.substring(11,12)) == 0 ){
                System.out.println("Le paquet envoye intended "+ paquet);
                paquet = paquet.replaceFirst("[e]", "s");
            }
            System.out.println("Le paquet envoye "+ paquet);
            //Transformation en bytes
            byte[] buf =  paquet.getBytes();

            // Envoi du paquet
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 25500);
            socketEnvoi.send(packet);

            paquetMalEnvoye = false;
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
        liaison.envoiReponseTransport(new String(packetReception.getData(), 0 , packetReception.getLength()));

    }
}


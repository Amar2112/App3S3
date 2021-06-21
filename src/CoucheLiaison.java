import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.lang.Integer;

public class CoucheLiaison {



    private Boolean stateConnexion;
    private CoucheTransportServeur transport;

    public CoucheLiaison(){
        transport = new CoucheTransportServeur(this);
        stateConnexion = true;
    }


    /**
     * Ajoute le checkSum dans le paquet Sortant
     * @param paquetEntrant
     * @return
     */
    public String populerPaquet(String paquetEntrant)
    {
       String paquetSortant = paquetEntrant + checkSum(paquetEntrant.getBytes()) ;

        return paquetSortant;
    }

    /**
     * Envoie à la couche transport
     * @param paquetEntrant
     * @return
     */
    public void recevoirPaquet(String paquetEntrant){
        String crc = paquetEntrant.substring((paquetEntrant.length()-10), paquetEntrant.length());

        //paquetEntrant.length()
        String donnes = paquetEntrant.substring(0, paquetEntrant.length()-10);
        if(!compareCRC(donnes,crc)){
            transport.getFromCoucheLiaison(paquetEntrant);
        }

        transport.demandeRenvoi(paquetEntrant);
    }
    /**
     * Envoi le paquet au serveur
     * @param paquetSortant
     */
    public void envoyerPaquetServeur(String paquetSortant, String adresseString){

        try{
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(adresseString);

            //Ajoute l'entête au paquet
            paquetSortant = populerPaquet(paquetSortant);

            //Transformation en bytes
            byte[] buf =  paquetSortant.getBytes();

            // Envoi du paquet
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 25500);
            socket.send(packet);
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void envoiReponseAuClient(String paquet){

    }

    /**
     * Calcule le checkSum
     * @param bytes
     * @return
     */
    public long checkSum(byte[] bytes) {
        Checksum checksum = new CRC32();

        // update the current checksum with the specified array of bytes
        checksum.update(bytes, 0, bytes.length);

        // get the current checksum value
        return checksum.getValue();
    }

    public Boolean compareCRC(String donnees, String crcClient){
        if(crcClient.equals( String.valueOf(checkSum(donnees.getBytes())))){
            return true;
        }
        return false;
    }

    public void couperConnexion(){
        stateConnexion = false;
    }

    public Boolean getStateConnexion() {
        return stateConnexion;
    }
}

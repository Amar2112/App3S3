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
    private CoucheTransport retour;
    private CouchePhysique physique;
    private String reponse;
    private String paquetSortant;

    public CoucheLiaison(){

        stateConnexion = true;
        reponse = null;
    }

    public void lierCouchePhysique(CouchePhysique physique){
        this.physique = physique;
    }

    public void lierCoucheTransport(CoucheTransport transport){
        this.retour = transport;
    }

    public void lierCoucheTransportServeur(CoucheTransportServeur serveur){
        this.transport = serveur;
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
            //System.out.println(donnes);
        }

        transport.demandeRenvoi(paquetEntrant);
    }
    /**
     * Envoi le paquet au serveur
     * @param paquetSortant
     */
    public void envoyerPaquetServeur(String paquetSortant, String adresseString){

            //Ajoute l'entête au paquet
            paquetSortant = populerPaquet(paquetSortant);
            physique.EnvoiServeur(paquetSortant,adresseString);


    }

    public void envoiReponseTransport (String donnees)
    {
        donnees.substring(0,donnees.length()-10);
        retour.retourLiaison(donnees);
    }

    public void envoiReponseAuClient(String paquet){

        reponse = paquet;
    }

    public String getReponseClient(){
        if(stateConnexion){
           return reponse;
        }
        return null;
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

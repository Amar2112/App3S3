import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CoucheLiaisonServeur {
    private Boolean stateConnexion;
    private CoucheTransportServeur transport;
    private CoucheTransport retour;
    private CouchePhysique physique;
    private String reponse;
    private String paquetSortant;

    Logger log;
    FileHandler fh;
    SimpleFormatter formatter;
    public CoucheLiaisonServeur(){

        try {
            log = Logger.getLogger(CoucheLiaison.class.getName());
            fh = new FileHandler("LiaisonDeDonnees.log",true);
            log.addHandler(fh);
            formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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
        String paquetSortant = paquetEntrant + checkSum(paquetEntrant.getBytes());
        System.out.println("Avec le crc " + paquetSortant);
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

        if(compareCRC(donnes,crc)){
            transport.getFromCoucheLiaison(donnes);
            System.out.println("Je rentre à bonne place");
            String logInfo = donnes.substring(12);
            log.info("Message reçu par le serveur : "+logInfo + "\n");
        }else{

            transport.demandeRenvoi(paquetEntrant);
        }
    }
    /**
     * Envoi le paquet au serveur
     * @param paquetSortant
     */
    public void envoyerPaquetServeur(String paquetSortant, String adresseString){

        //Ajoute l'entête au paquet
        paquetSortant = populerPaquet(paquetSortant);
        physique.EnvoiServeur(paquetSortant,adresseString);

        String logInfo = paquetSortant.substring(12);
        logInfo = logInfo.substring(0,logInfo.length()-10);

        log.info(logInfo + " envoyé vers : " + adresseString + "\n");
    }

    public void envoiReponseTransport (String donnees)
    {
        donnees.substring(0,donnees.length()-10);
        retour.retourLiaison(donnees);
    }

    public void envoiReponseAuClient(String paquet){
        System.out.println("Reponse à renvoyer"+paquet);
        log.info("Réponse à renvoyer : " + paquet);
        reponse = paquet;
    }

    public String getReponseClient(){
        return reponse;
    }

    /**
     * Calcule le checkSum
     * @param bytes
     * @return
     */
    public String checkSum(byte[] bytes) {
        Checksum checksum = new CRC32();

        // update the current checksum with the specified array of bytes
        checksum.update(bytes, 0, bytes.length);

        //Padding de tout
        String stringCheckSum = "";
        if(10 - String.valueOf(checksum.getValue()).length() != 0){
            String nombreDeDigitsValeurCheckSum = "%0" + (10) + "d";
            stringCheckSum = String.format(nombreDeDigitsValeurCheckSum, checksum.getValue());
        }
        else{
            stringCheckSum =  String.valueOf(checksum.getValue());
        }
        // get the current checksum value
        return stringCheckSum;
    }

    public Boolean compareCRC(String donnees, String crcClient){
        if(crcClient.equals(checkSum(donnees.getBytes()))){
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

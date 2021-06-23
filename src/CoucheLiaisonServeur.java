import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CoucheLiaisonServeur implements CoucheLiaison {
    private Boolean stateConnexion;
    private CoucheTransportServeur transport;
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

    /**
     * Instancier la couche physique
     * @param physique
     */
    public void lierCouchePhysique(CouchePhysique physique){
        this.physique = physique;
    }

    /**
     * Instancie la couche transport
     * @param serveur
     */
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

    /**
     * Envoi la bonne reponse à renvoyer
     * @param paquet
     */
    public void envoiReponseAuClient(String paquet){
        log.info("Réponse à renvoyer : " + paquet);
        reponse = paquet;
    }

    /**
     * Getter du acknowledge
     * @return
     */
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

    /**
     * Compare les 2 crc
     * @param donnees les donnees du client
     * @param crcClient le crc du client
     * @return
     */
    public Boolean compareCRC(String donnees, String crcClient){
        if(crcClient.equals(checkSum(donnees.getBytes()))){
            return true;
        }
        return false;
    }

    /**
     * Coupe la connexion avec le client
     */
    public void couperConnexion(){
        stateConnexion = false;
    }

    /**
     * Getter de l'état de la connexion
     * @return
     */
    public Boolean getStateConnexion() {
        return stateConnexion;
    }
}

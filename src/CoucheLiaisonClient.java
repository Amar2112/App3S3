import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CoucheLiaisonClient implements CoucheLiaison {
    private Boolean stateConnexion;
    private CoucheTransport retour;
    private CouchePhysique physique;
    private String reponse;
    private String paquetSortant;

    Logger log;
    FileHandler fh;
    SimpleFormatter formatter;
    public CoucheLiaisonClient(){

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
     * Instancie la couche physique
     * @param physique
     */
    public void lierCouchePhysique(CouchePhysique physique){
        this.physique = physique;
    }

    /**
     * Instancie la couche transport
     * @param transport
     */
    public void lierCoucheTransport(CoucheTransport transport){
        this.retour = transport;
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
     * Envoi la reponse à la couche transport
     * @param donnees
     */
    public void envoiReponseTransport (String donnees)
    {
        donnees.substring(0,donnees.length()-10);
        retour.retourLiaison(donnees);
    }

    /**
     * Envoi la reponse au client
     * @param paquet
     */
    public void envoiReponseAuClient(String paquet){
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

    /**
     * Compare les crc
     * @param donnees les donnees recues
     * @param crcClient le crc des donnees recues
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
     * Renvoie l'état de la connexion avec le client
     * @return
     */
    public Boolean getStateConnexion() {
        return stateConnexion;
    }
}

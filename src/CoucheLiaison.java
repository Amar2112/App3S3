import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public interface CoucheLiaison {

    /**
     * Instancier la couche physique
     * @param physique
     */
    public void lierCouchePhysique(CouchePhysique physique);
    /**
     * Ajoute le checkSum dans le paquet Sortant
     * @param paquetEntrant
     * @return
     */
    public String populerPaquet(String paquetEntrant);
    /**
     * Envoi le paquet au serveur
     * @param paquetSortant
     */

    /**
     * Envoi le paquet à la couche physique
     * @param paquetSortant
     * @param adresseString
     */
    public void envoyerPaquetServeur(String paquetSortant, String adresseString);

    /**
     *
     * @param paquet
     */
    public void envoiReponseAuClient(String paquet);

    /**
     * Redonne la reponse à renvoyer au client
     * @return
     */
    public String getReponseClient();

    /**
     * Calcule le checkSum
     * @param bytes
     * @return
     */
    public String checkSum(byte[] bytes);

    /**
     * Compare les 2 crc
     * @param donnees
     * @param crcClient
     * @return
     */
    public Boolean compareCRC(String donnees, String crcClient);

    /**
     * Coupe la connexion avec le client
     */
    public void couperConnexion();

    /**
     * Regarde si la connexion clinet-serveur est active
     * @return
     */
    public Boolean getStateConnexion() ;
}

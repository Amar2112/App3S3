import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public interface CoucheLiaison {

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
    public void envoyerPaquetServeur(String paquetSortant, String adresseString);

    public void envoiReponseAuClient(String paquet);

    public String getReponseClient();

    /**
     * Calcule le checkSum
     * @param bytes
     * @return
     */
    public String checkSum(byte[] bytes);

    public Boolean compareCRC(String donnees, String crcClient);
    public void couperConnexion();

    public Boolean getStateConnexion() ;
}

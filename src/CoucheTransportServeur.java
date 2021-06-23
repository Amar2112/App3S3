import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class CoucheTransportServeur {
    private final int connexionPerdue = 3;
    private int dernierPaquetRecu;
    private int compteurDemande;
    private String listeDePaquet[];
    private int nombreDePaquetsRecus;
    private CoucheLiaisonServeur coucheLiaison;
    private boolean premiereDemande;
    private CoucheApplication coucheApplication;


    public CoucheTransportServeur(){
        premiereDemande = true;
        dernierPaquetRecu = 0;
        compteurDemande = 0;
        nombreDePaquetsRecus = 0;
    }

    /**
     * Reinitialise les valeurs pour les erreurs à leur état initial et vide le tableau des donnees
     */
    public void reinitialiser(){
        premiereDemande = true;
        dernierPaquetRecu = 0;
        compteurDemande = 0;
        nombreDePaquetsRecus = 0;
        listeDePaquet = null;
    }

    /**
     * Instncie liaison
     * @param liaison
     */
    public void lierAvecLiaison(CoucheLiaisonServeur liaison){
        coucheLiaison = liaison;
    }
    /**
     * Coupe la connexion avec le client
     */
    public void couperConnexion(){
        coucheLiaison.couperConnexion();
    }

    /**
     * Demande un renvoi au Client
     * @param paquetDemande
     */
    public void demandeRenvoi(String paquetDemande){
        compteurDemande ++;
        String renvoi = paquetDemande.substring(0,11) + "1" + "Le paquet doit être renvoye";
        coucheLiaison.envoiReponseAuClient(renvoi);
    }

    /**
     * Renvoie que la connexion est perdue au client
     * @param paquetDemande
     */
    public void demandeConnexionPerdue(String paquetDemande){
        String renvoi = paquetDemande.substring(0,11) + "2" + "La connexion est perdue";
        coucheLiaison.envoiReponseAuClient(renvoi);
        envoyerCoucheApplication(listeDePaquet);
    }

    /**
     * Envoyer le tableau de string à la couche application
     * @param totalPaquets
     */
    public void envoyerCoucheApplication(String [] totalPaquets) //throws TransmissionErrorException
     {
         coucheApplication.writeInFile(totalPaquets);
     }

    /**
     * Acknowledge la demande
     * @param paquetAccepte
     */
    public void demandeAcceptee(String paquetAccepte){
        String acknowledge;
       premiereDemande = false;
        if(listeDePaquet[Integer.parseInt(paquetAccepte.substring(0,4)) -1] != null ) {
            acknowledge = paquetAccepte.substring(0,11) + "0" + "Le paquet a déjà été reçu" ;
        }
        else{
            listeDePaquet[Integer.parseInt(paquetAccepte.substring(0, 4)) - 1] = paquetAccepte.substring(12);

            nombreDePaquetsRecus++;
            dernierPaquetRecu ++;
            acknowledge = paquetAccepte.substring(0,11) + "0" + "Le paquet a ete reçu" ;
        }
        coucheLiaison.envoiReponseAuClient(acknowledge);
        if(nombreDePaquetsRecus == Integer.parseInt(paquetAccepte.substring(4, 8))){
            couperConnexion();
            envoyerCoucheApplication(listeDePaquet);
        }
    }

    /**
     * Traite les paquets la première fois qu'ils sont envoyés
     * @param paquet
     */
    public void TraiterPaquetEnvoi(String paquet){
        int paquetActuel = Integer.parseInt(paquet.substring(0,4));


        if(listeDePaquet[Integer.parseInt(paquet.substring(0,4)) -1] != null ) {
            String acknowledge = paquet.substring(0,11) + "0" + "Le paquet a déjà été reçu" ;
            coucheLiaison.envoiReponseAuClient(acknowledge);
        }
        else{
            if((paquetActuel - dernierPaquetRecu) == 1){
                demandeAcceptee(paquet);
            }else{

                if(compteurDemande == connexionPerdue){
                    demandeConnexionPerdue(paquet);
                    //couperConnexion();
                }
                else{
                    String numeroPaquetDebut = "";
                    if(3 - String.valueOf(Integer.parseInt(paquet.substring(0,4)) -1).length() != 0) {
                        String nombreDeDigitsPaquetDebut = "%0" + (4) + "d";
                        numeroPaquetDebut = String.format(nombreDeDigitsPaquetDebut, Integer.parseInt(paquet.substring(0,4)) -1);
                    }
                    paquet = numeroPaquetDebut + paquet.substring(4,paquet.length());
                    demandeRenvoi(paquet);
                }
        }
        }
    }

    /**
     * Gère les erreurs de transmission
     * @param paquet
     */
    public void getFromCoucheLiaison(String paquet){
        if(premiereDemande == true){
        listeDePaquet = new String[Integer.parseInt(paquet.substring(4,8))];
    }
        TraiterPaquetEnvoi(paquet);
    }

    public void lierCoucheApplication(CoucheApplication appli){
        this.coucheApplication = appli;
    }
}

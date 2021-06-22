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
    private CoucheLiaison coucheLiaison;
    private CoucheApplication coucheApplication;


    public CoucheTransportServeur(){
        dernierPaquetRecu = 0;
        compteurDemande = 0;
        nombreDePaquetsRecus = 0;
    }

    public void lierAvecLiaison(CoucheLiaison liaison){
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
        String renvoi = paquetDemande.substring(0,11) + "1" + "Le paquet doit être renvoye";
        //System.out.println("Allo");
        coucheLiaison.envoiReponseAuClient(renvoi);
    }

    public void demandeConnexionPerdue(String paquetDemande){
        String renvoi = paquetDemande.substring(0,11) + "2" + "La connexion est perdue";
        //System.out.println("Allo");
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
        
        if(listeDePaquet[Integer.parseInt(paquetAccepte.substring(0,4)) -1] != null ) {
            acknowledge = paquetAccepte.substring(0,11) + "0" + "Le paquet a déjà été reçu" ;
        }
        else{
            listeDePaquet[Integer.parseInt(paquetAccepte.substring(0, 4)) - 1] = paquetAccepte.substring(12);
            nombreDePaquetsRecus++;
            acknowledge = paquetAccepte.substring(0,11) + "0" + "Le paquet a ete reçu" ;
        }
        System.out.println("Yes" +  listeDePaquet[Integer.parseInt(paquetAccepte.substring(0,4)) -1 ] );
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


        if( (paquetActuel - dernierPaquetRecu) == 1){


            demandeAcceptee(paquet);
            if(Integer.parseInt(paquet.substring(11,12)) == 0){
                dernierPaquetRecu = paquetActuel;
            }

        }else{

            if(compteurDemande == connexionPerdue){
                demandeConnexionPerdue(paquet);
                //couperConnexion();
            }
            else{
                compteurDemande ++;
                String numeroPaquetDebut = "";
                if(3 - String.valueOf(Integer.parseInt(paquet.substring(0,4)) -1).length() != 0) {
                    String nombreDeDigitsPaquetDebut = "%0" + (4) + "d";
                    numeroPaquetDebut = String.format(nombreDeDigitsPaquetDebut, Integer.parseInt(paquet.substring(0,4)) -1);
                }
                System.out.println("Le paquet à renvoyer " + numeroPaquetDebut);
                paquet = numeroPaquetDebut + paquet.substring(4,paquet.length());
                demandeRenvoi(paquet);
            }


        }
    }

    /**
     * Gère les erreurs de transmission
     * @param paquet
     */
    public void getFromCoucheLiaison(String paquet){
        if(nombreDePaquetsRecus == 0){
            listeDePaquet = new String[Integer.parseInt(paquet.substring(4,8))];
        }

        TraiterPaquetEnvoi(paquet);
    }

    public void lierCoucheApplication(CoucheApplication appli){
        this.coucheApplication = appli;
    }
}

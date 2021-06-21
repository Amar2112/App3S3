public class CoucheTransportServeur {
    private final int connexionPerdue = 3;
    private int dernierPaquetRecu;
    private int compteurDemande;
    private String listeDePaquet[];
    private CoucheLiaison coucheLiaison;

    public CoucheTransportServeur(){
        dernierPaquetRecu = 0;
        compteurDemande = 1;
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

    /**
     * Acknowledge la demande
     * @param paquetAccepte
     */
    public void demandeAcceptee(String paquetAccepte){


        if(listeDePaquet[Integer.parseInt(paquetAccepte.substring(0,4)) -1] == null)
        listeDePaquet[Integer.parseInt(paquetAccepte.substring(0,4)) -1] = paquetAccepte;
        String acknowledge = paquetAccepte.substring(0,11) + "0" + "Le paquet a ete reçu" ;
        System.out.println("Yes" +  listeDePaquet[Integer.parseInt(paquetAccepte.substring(0,4)) -1 ] );
        coucheLiaison.envoiReponseAuClient(acknowledge);

    }

    /**
     * Traite les paquets la première fois qu'ils sont envoyés
     * @param paquet
     */
    public void TraiterPaquetEnvoi(String paquet){
        int paquetActuel = Integer.parseInt(paquet.substring(0,4));

        System.out.println("Numero "+ paquetActuel);
        System.out.println("Numero2 "+ dernierPaquetRecu);

        System.out.println(paquet);

        if( (paquetActuel - dernierPaquetRecu) == 1){

            demandeAcceptee(paquet);
            dernierPaquetRecu = paquetActuel;
        }else{
            System.out.println("Allo");
            demandeRenvoi(paquet);
        }
    }

    /**
     * Traite les paquets quand ils sont renvoyés
     * @param paquet
     */
    public void traiterPaquetRenvoi(String paquet){
        int paquetActuel = Integer.parseInt(paquet.substring(0,4));
        if(paquetActuel == dernierPaquetRecu + 1){
            demandeAcceptee(paquet);
            dernierPaquetRecu ++;
        }else{
            demandeRenvoi(paquet);
            if(compteurDemande != 3)
            compteurDemande++;
            else couperConnexion();
        }
    }
    /**
     * Gère les erreurs de transmission
     * @param paquet
     */
    public void getFromCoucheLiaison(String paquet){
        listeDePaquet = new String[Integer.parseInt(paquet.substring(4,8))];

        if(Integer.parseInt(paquet.substring(11,12)) == 0){
           // System.out.println(paquet);
            TraiterPaquetEnvoi(paquet);
        }
        else{
            traiterPaquetRenvoi(paquet);
        }
    }
}

public class CoucheTransport {

    private CoucheLiaisonClient liaison;
    private String adresse;
    private String[] listePaquetsAEnvoyer;
    private int compteur;
    private boolean reception;
    private boolean troisErreurs;
    private boolean envoiInverse;

    public CoucheTransport(){
        compteur = 0;
        reception = true;
        adresse = new String();
        troisErreurs = false;
        envoiInverse = false;
    }

    public void lierCoucheLiaison(CoucheLiaisonClient liaison){
        this.liaison = liaison;
    }
    /**
     * Envoi à la couche de transport
     * @param paquetEntrant
     * @param fichierNom
     */
    public void envoiLiaison(String paquetEntrant, String fichierNom, String adresse) {
        this.adresse = adresse;


        String [] paquetsAEnvoyer = paquetFragmenteEntete(paquetEntrant,fichierNom);
        listePaquetsAEnvoyer = paquetsAEnvoyer;

        if(troisErreurs == true ){
            liaison.envoyerPaquetServeur(paquetsAEnvoyer[6], adresse);
        }
        if(envoiInverse == true){
            liaison.envoyerPaquetServeur(paquetsAEnvoyer[1], adresse);
        }


        while(compteur < paquetsAEnvoyer.length && reception){
            liaison.envoyerPaquetServeur(paquetsAEnvoyer[compteur], adresse);
            compteur++;
        }
        
    }

    /**
     * Permet de retourner le paquet à la couche de liaison
     * @param donnees 0 pour acceptee, 1 pour renvoie et 2 pour connexion perdue
     */
    public void retourLiaison(String donnees){
        if(Integer.parseInt(donnees.substring(11,12)) == 1){
            String paquetARenvoyer = listePaquetsAEnvoyer[Integer.parseInt(donnees.substring(0,4)) -1].substring(0,11) + "1" + listePaquetsAEnvoyer[Integer.parseInt(donnees.substring(0,4)) -1].substring(12);
            liaison.envoyerPaquetServeur(paquetARenvoyer, adresse);
        }else if(Integer.parseInt(donnees.substring(11,12)) == 2) {
            reception = false;
        }
    }

    /**
     * Trigger d'une erreur qui coupe la connexion avec le client, il faut que le texte ait plus que 1200 bytes
     */
    public void triggerErreur3Fois(){
        troisErreurs = true;
    }

    /**
     * Envoi le paquet numero 2 en premier
     */
    public void envoiInverse(){ envoiInverse = true;}

    /**
     * Deduit le nombre de paquets à fragmenter pour 200 Bytes
     *
     * @param paquetEntrant
     * @return
     */
    public int nombrePaquet(String paquetEntrant) {
        int dernierPaquet = paquetEntrant.getBytes().length % 200;
        if (dernierPaquet == 0) {
            return paquetEntrant.getBytes().length / 200;
        }
        return ((paquetEntrant.getBytes().length - dernierPaquet) / 200) + 1;
    }

    /**
     * Fragmente le paquet en petits paquets de 200
     */
    public String[] paquetsFragmentes(String paquetEntrant) {
        String[] listePaquetsAEnvoyer = new String[nombrePaquet(paquetEntrant)];

        for (int i = 0; i < nombrePaquet(paquetEntrant); i++) {
            if (i == nombrePaquet(paquetEntrant) - 1) {
                listePaquetsAEnvoyer[i] = new String(paquetEntrant.getBytes(), i * 200,  paquetEntrant.getBytes().length % 200);
            } else {
                listePaquetsAEnvoyer[i] = new String(paquetEntrant.getBytes(), i * 200,  200);
            }
        }

        return listePaquetsAEnvoyer;
    }

    /**
     * Crée l'entête pour les fragments
     * @param paquetEntrant
     * @param fichierNom
     * @return
     */
    public String[] paquetFragmenteEntete(String paquetEntrant, String fichierNom) {
        int nombrePaquetAFragmenter = nombrePaquet(paquetEntrant) + 1;

        //Contient tous les paquets à envoyer avec l'entête et le nom du fichier
        String paquetsFragementesListe[] = new String[nombrePaquetAFragmenter];

        String paquetsSansEntete[] = paquetsFragmentes(paquetEntrant);

        String enteteNombrePaquet = "";//Nombre de paquets totaux que comporte le fichier

        //Remplissage de zéros pour le nombre de paquets
        if(4 - String.valueOf(nombrePaquetAFragmenter).length() != 0){
            String nombreDeDigits = "%0" + (4) + "d";
            enteteNombrePaquet = String.format(nombreDeDigits, nombrePaquetAFragmenter);
        }else{
            enteteNombrePaquet = String.valueOf(nombrePaquetAFragmenter);
        }


        String enteteLongueurFichier = "";
        //Remplissage de zéros pour la longueur du fichier
        if(3 - String.valueOf(fichierNom.getBytes().length).length() != 0) {
            String nombreDeDigitsFichier = "%0" + (3) + "d";
            enteteLongueurFichier = String.format(nombreDeDigitsFichier, fichierNom.getBytes().length);
        }else{
            enteteLongueurFichier = String.valueOf(fichierNom.getBytes().length);
        }
        //Paquet avec le nom dyu fichier seulement
        paquetsFragementesListe[0] = "0001" + enteteNombrePaquet + enteteLongueurFichier + "0" + fichierNom;


        for (int i = 1; i < nombrePaquetAFragmenter; i++) {
            //Remplissage de zéros pour le paquet actuel
            String enteteNombrePaquetFragment = "0000";

            if(4 - String.valueOf(i).length() != 0){
                String nombreDeDigitsFragmentActuel = "%0" + (4) + "d";
                enteteNombrePaquetFragment = String.format(nombreDeDigitsFragmentActuel, i +1);
            }
            else{
                enteteNombrePaquetFragment = String.valueOf(i);
            }

            //Remplissage de zéros pour la longueur du dernier paquet
            String enteteLongueurFichierFragment;
            if (nombrePaquetAFragmenter - 1 == i){
                if(3 - String.valueOf(paquetsSansEntete[i - 1].getBytes().length).length() != 0) {
                    String nombreDeDigitsFragmentTaille = "%0" + (3) + "d";

                    enteteLongueurFichierFragment = String.format(nombreDeDigitsFragmentTaille, paquetsSansEntete[i - 1].getBytes().length);
                }
                else{
                    enteteLongueurFichierFragment = String.valueOf(paquetsSansEntete[i - 1].getBytes().length);
                }
                }
            else{
                enteteLongueurFichierFragment = "200";
            }

            paquetsFragementesListe[i] = enteteNombrePaquetFragment + enteteNombrePaquet + enteteLongueurFichierFragment + "0" + paquetsSansEntete[i-1];

        }


        return paquetsFragementesListe;
    }
}

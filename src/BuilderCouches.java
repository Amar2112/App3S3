

public class BuilderCouches implements Builder {
    private CoucheLiaisonClient cLC;
    private CoucheTransport cT;
    private CouchePhysique cP;

    public BuilderCouches(){
        stepA();
        stepB();
    }

    /**
     * La première étape permet de créer les objets nécessaires au bon fonctionnement du builder
     */
    @Override
    public void stepA() {
        cLC = new CoucheLiaisonClient();
        cT = new CoucheTransport();
        cP = new CouchePhysique();
    }

    /**
     * La deuxième étape permet de lier les objets créés ensemble
     */
    @Override
    public void stepB() {
        //Liaison de la couche liaison
        cLC.lierCouchePhysique(cP);
        cLC.lierCoucheTransport(cT);
        //Liaison de la couche physique
        cP.lierCoucheLiaison(cLC);
        //Liaison de la couche transport
        cT.lierCoucheLiaison(cLC);
    }

    public void changerCRC(){ cP.paquetMalEnvoye();}

    public void envoiInverse(){
        cT.envoiInverse();
    }

    public void envoi3erreurs(){
        cT.triggerErreur3Fois();
    }

    /**
     *
     * @param fichier Le fichier qui doit être envoyé
     * @param nomFichier Le nom du fichier
     * @param adresse L'adresse IP de l'ordinateur que nous allons utiliser
     */
    public void sendThroughTransport(String fichier, String nomFichier, String adresse) {
        cT.envoiLiaison(fichier,nomFichier,adresse);
    }
}

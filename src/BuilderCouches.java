

public class BuilderCouches implements Builder {
    private CoucheLiaisonClient cLC;
    private CoucheTransport cT;
    private CouchePhysique cP;

    public BuilderCouches(){
        stepA();
        stepB();
    }


    @Override
    public void stepA() {
        cLC = new CoucheLiaisonClient();
        cT = new CoucheTransport();
        cP = new CouchePhysique();
    }

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

    public void sendThroughTransport(String fichier, String nomFichier, String adresse) {
        cT.envoiLiaison(fichier,nomFichier,adresse);
    }
}

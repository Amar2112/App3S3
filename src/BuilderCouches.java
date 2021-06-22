

public class BuilderCouches implements Builder {
    private CoucheLiaison cL;
    private CoucheTransport cT;
    private CouchePhysique cP;

    public BuilderCouches(){
        stepA();
        stepB();
    }

    @Override
    public void stepA() {
        cL = new CoucheLiaison();
        cT = new CoucheTransport();
        cP = new CouchePhysique();
    }

    @Override
    public void stepB() {
        //Liaison de la couche liaison
        cL.lierCouchePhysique(cP);
        cL.lierCoucheTransport(cT);
        //Liaison de la couche physique
        cP.lierCoucheLiaison(cL);
        //Liaison de la couche transport
        cT.lierCoucheLiaison(cL);
    }

    public void sendThroughTransport(String fichier, String nomFichier, String adresse) {
        cT.envoiLiaison(fichier,nomFichier,adresse);
    }
}

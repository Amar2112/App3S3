public class BuilderServeur implements Builder{

    private CoucheLiaisonServeur liaison;
    private CoucheTransportServeur transportServeur;
    private CoucheApplication application;

    public BuilderServeur(){
        stepA();
        stepB();
    }

    @Override
    public void stepA() {
        this.liaison = new CoucheLiaisonServeur();
        this.transportServeur = new CoucheTransportServeur();
        this.application = new CoucheApplication();
    }

    @Override
    public void stepB() {
        this.liaison.lierCoucheTransportServeur(transportServeur);
        this.transportServeur.lierAvecLiaison(liaison);
        this.transportServeur.lierCoucheApplication(application);
        this.application.lierCoucheTransportServeur(transportServeur);
    }

    public void recevoirPaquet(String packet){
        liaison.recevoirPaquet(packet);
    }
    public boolean getStateConnection(){
        return liaison.getStateConnexion();
    }

    public String getReponseClient(){
        return liaison.getReponseClient();
    }

}

public class BuilderServeur implements Builder{

    private CoucheLiaison liaison;
    private CoucheTransportServeur transportServeur;
    private CoucheApplication application;

    public BuilderServeur(){
        stepA();
        stepB();
    }

    @Override
    public void stepA() {
        this.liaison = new CoucheLiaison();
        this.transportServeur = new CoucheTransportServeur();
        this.application = new CoucheApplication();
    }

    @Override
    public void stepB() {
        this.liaison.lierCoucheTransportServeur(transportServeur);
        this.transportServeur.lierAvecLiaison(liaison);
        this.transportServeur.lierCoucheApplication(application);
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
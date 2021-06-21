public class Tests {

    public void testCoucheLiaison(String nomAdresse){
        CoucheLiaison coucheLiaison = new CoucheLiaison();

        // send request
        String message = "patate";

        coucheLiaison.envoyerPaquetServeur(message, nomAdresse);
    }

    public void testCoucheTransport(String nomAdresse){
        /*CoucheTransport coucheTransport = new CoucheTransport();

        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam blandit justo nisl, sit amet convallis lectus facilisis at. Sed ultrices lobortis dapibus. Nam scelerisque eros volutpat, cursus dui vel, feugiat nisl. Morbi diam enim, tempus vel eros sed, vestibulum blandit mi. Maecenas semper turpis.";
        String nomFichier = "la vie va bien";
        coucheTransport.EnvoiTransport(message, nomFichier, nomAdresse);*/
    }
}

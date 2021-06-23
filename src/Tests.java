public class Tests {


    /**
     * Test pour le bon fonctionnement
     * @param appScan
     */
    public void testQuiMarche(BuilderApplicationScan appScan){
        BuilderCouches couches = new BuilderCouches();
        couches.sendThroughTransport(appScan.getFichier(), appScan.getName(),appScan.getDestinationIP());
    }

    /**
     * Test pour le crc qui n'est pas pareil
     * @param appScan
     */
    public void testCRCMarchePas(BuilderApplicationScan appScan){
        BuilderCouches couches = new BuilderCouches();
        couches.changerCRC();
        couches.sendThroughTransport(appScan.getFichier(), appScan.getName(),appScan.getDestinationIP());
    }

    /**
     * L'envoi ne se fait pas dans le bon ordre
     * @param appScan
     */
    public void testEnvoiPasBonOrdre(BuilderApplicationScan appScan){
        BuilderCouches couches = new BuilderCouches();
        couches.envoiInverse();
        couches.sendThroughTransport(appScan.getFichier(), appScan.getName(),appScan.getDestinationIP());
    }

    /**
     * Appelle le serveut avec 3 erreurs
     * @param appScan
     */
    public void test3Erreurs(BuilderApplicationScan appScan){
        BuilderCouches couches = new BuilderCouches();
        couches.envoi3erreurs();
        couches.sendThroughTransport(appScan.getFichier(), appScan.getName(),appScan.getDestinationIP());
    }

}

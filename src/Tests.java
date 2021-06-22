public class Tests {


    public void testQuiMarche(BuilderApplicationScan appScan){
        BuilderCouches couches = new BuilderCouches();
        couches.sendThroughTransport(appScan.getFichier(), appScan.getName(),appScan.getDestinationIP());
    }

    public void testCRCMarchePas(BuilderApplicationScan appScan){
        BuilderCouches couches = new BuilderCouches();
        couches.changerCRC();
        couches.sendThroughTransport(appScan.getFichier(), appScan.getName(),appScan.getDestinationIP());
    }

    public void testEnvoiPasBonOrdre(BuilderApplicationScan appScan){
        BuilderCouches couches = new BuilderCouches();
        couches.envoiInverse();
        couches.sendThroughTransport(appScan.getFichier(), appScan.getName(),appScan.getDestinationIP());
    }

    public void test3Erreurs(BuilderApplicationScan appScan){
        BuilderCouches couches = new BuilderCouches();
        couches.envoi3erreurs();
        couches.sendThroughTransport(appScan.getFichier(), appScan.getName(),appScan.getDestinationIP());
    }

}

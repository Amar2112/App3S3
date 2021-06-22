public class CoucheFactory {
    public Couche createCouche(String type) {
        if(type == "Liaison")
        {
            return new CoucheLiaison();
        }
        else if (type == "TransportServeur"){
            return new CoucheTransportServeur();
        }
        return null;
    }
}

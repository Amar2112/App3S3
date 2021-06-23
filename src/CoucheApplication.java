import java.io.*;
import java.util.Date;

public class CoucheApplication {

    private String name;
    private String destinationIP;
    private FileWriter myWriter;
    private CoucheTransportServeur transportServeur;

    public CoucheApplication(){

    }
    public CoucheApplication(String nom, String destIP)
    {
        this.name = nom;
        this.destinationIP = destIP;
    }

    public void lierCoucheTransportServeur(CoucheTransportServeur serveur){
        transportServeur = serveur;
    }
    /***
     * Fonction qui permet de de prendre en argument soit juste le nom du fichier ou encore un URL contenant le répertoire
     * @param nomFichier
     * @return
     */
    public String lireFichier(String nomFichier){
        String donnees = "";
        setName(nomFichier);
        try {
            BufferedReader br;

            //On vérifie si le nom du fichier est bien écrit sinon on ajuste
            if(!nomFichier.contains(".txt"))
            {
                br = new BufferedReader(new FileReader(nomFichier + ".txt"));
            }
            else
            {
                br = new BufferedReader(new FileReader(nomFichier));
            }

            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                String everything = sb.toString();
                donnees =  everything;
            } catch (IOException i) {
                i.printStackTrace();
            } finally {
                br.close();
            }
        }
        catch (IOException i){
        i.printStackTrace();
        }
        return donnees;
    }

    public void reinitialiserCouche(){
        transportServeur.reinitialiser();
    }

    /**
     * Permet d'écrire dans le fichier physique ce que nous avons reçu comme message.
     * @param donnees Contient le nom du fichier reçu ainsi que son contenu.
     */
    public void writeInFile(String[] donnees) {
        boolean quelqueChoseEcrit = true;
        for(int i = 0; i< donnees.length; i++){
            if (donnees[i] == null){
                quelqueChoseEcrit = false;
            }
        }

        if(quelqueChoseEcrit == true){
            try {
                myWriter = new FileWriter("exampleFile.txt",true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < donnees.length; i++) {
                try {
                    if (i != 0) {
                        myWriter.append(donnees[i]);
                    } else {
                        Date date = new Date();
                        myWriter.append(date.toString() + "\n");
                        myWriter.append("Name of the file : " + donnees[i] + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            closeFile();
        }

        reinitialiserCouche();

    }

    /**
     * Permet de fermer le fichier pour bien enregistrer le contenu.
     */
    public void closeFile(){
        try {
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    public void setDestinationIP(String destinationIP) {
        this.destinationIP = destinationIP;
    }
}

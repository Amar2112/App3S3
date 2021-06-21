import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class CoucheApplication {

    private String name;
    private String destinationIP;

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

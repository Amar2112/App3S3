import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CoucheApplication {
    private String lireFichier(String nomFichier){
        String donnees = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(nomFichier + ".txt"));
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
}

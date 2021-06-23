import java.io.File;
import java.util.Scanner;

public class BuilderApplicationScan implements Builder {

    private Scanner scan;
    private File f;
    private String dataInput = null;
    private String[] buffer = null;
    private CoucheApplication cA;

    public BuilderApplicationScan(){
        stepA();
        stepB();
    }

    /**
     * La première étape permet de créer les objets nécessaires au bon fonctionnement du builder
     */
    @Override
    public void stepA() {

        scan = new Scanner(System.in);
        dataInput = scan.nextLine();
    }

    /**
     * La deuxième étape permet de lier les objets créés ensemble
     */
    @Override
    public void stepB() {
        buffer = dataInput.split(" ");
        f = new File(buffer[0]);
        cA = new CoucheApplication(f.getName(),buffer[1]);
    }

    public String getFichier(){
        return cA.lireFichier(buffer[0]);
    }

    public String getName(){ return cA.getName(); }

    public String getDestinationIP(){ return cA.getDestinationIP(); }
}

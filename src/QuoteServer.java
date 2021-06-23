import java.io.*;

public class QuoteServer {
    /**
     * Part le serveur
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new QuoteServerThread().start();
    }
    }

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class QuoteClient {


    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage: java QuoteClient <hostname>");
            return;
        }

        CouchePhysique couchePhysique = new CouchePhysique();
        CoucheLiaison coucheLiaison = new CoucheLiaison();
        CoucheTransport coucheTransport = new CoucheTransport();

        //Liaison de la couche liaison
        coucheLiaison.lierCouchePhysique(couchePhysique);
        coucheLiaison.lierCoucheTransport(coucheTransport);

        //Liaison de la couche physique
        couchePhysique.lierCoucheLiaison(coucheLiaison);
        //couchePhysique.paquetMalEnvoye();

        //Liaison de la couche transport
        coucheTransport.lierCoucheLiaison(coucheLiaison);

        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam blandit justo nisl, sit amet convallis lectus facilisis at. Sed ultrices lobortis dapibus. Nam scelerisque eros volutpat, cursus dui vel, feugiat nisl. Morbi diam enim, tempus vel eros sed, vestibulum blandit mi. Maecenas semper turpis.";
        String nomFichier = "la vie va bien";
        //coucheTransport.envoiLiaison(message, nomFichier, args[0]);

        Scanner scan = new Scanner(System.in);
        String test = scan.nextLine();
        String[] buffer = test.split(" ");
        File f = new File(buffer[0]);
        CoucheApplication coucheApplication = new CoucheApplication(f.getName(),buffer[1]);
        coucheTransport.envoiLiaison(coucheApplication.lireFichier(buffer[0]),coucheApplication.getName(),coucheApplication.getDestinationIP());



    }


}

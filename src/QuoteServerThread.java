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
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QuoteServerThread extends Thread {

    protected DatagramSocket socket = null;
    protected DatagramSocket socketEnvoi = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
    private CoucheLiaison liaison;
    private CoucheTransportServeur transportServeur;

    public QuoteServerThread() throws IOException {
        this("QuoteServerThread");
    }

    public QuoteServerThread(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(25500);
        socketEnvoi = new DatagramSocket();
        liaison = new CoucheLiaison();
        transportServeur = new CoucheTransportServeur();
        liaison.lierCoucheTransportServeur(transportServeur);
        transportServeur.lierAvecLiaison(liaison);
    }


    public void run() {
        boolean finDeLaTransmission = true;
        try{

        byte[] buf = new byte[256];
            // receive request
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while(liaison.getStateConnexion()  && finDeLaTransmission){
                socket.receive(packet);
                System.out.println("Avant Envoi -------------------------------");
                System.out.println("Avant le if"+new String(packet.getData(),0,packet.getLength()));
                liaison.recevoirPaquet(new String(packet.getData(),0,packet.getLength()));

                String donneesEnvoyer = liaison.getReponseClient();
                    if(donneesEnvoyer != null){
                        socketEnvoi.connect(packet.getAddress(), 25501);
                        byte [] transformationDonnees = donneesEnvoyer.getBytes();
                        DatagramPacket paquetEnvoyer = new DatagramPacket(transformationDonnees, transformationDonnees.length, packet.getAddress(), 25501);
                        socketEnvoi.send(paquetEnvoyer);
                        System.out.println("Après Envoi -------------------------------");
                    }

            }


        }catch (IOException i){
            socket.close();
            socketEnvoi.close();
            i.printStackTrace();

        }

        socketEnvoi.close();
        socket.close();
    }
}
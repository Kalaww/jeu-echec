package echecs.reseau.client;

import java.net.*;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;
/**
 * Classe ClientOut qui gere le flux de sortie du Client
 */
public class ClientOut extends Thread{
    Socket socket;
    OutputStream os;
    PrintWriter out;
    /**
     * Constructeur de la classe
     * @param s Le Socket
     */
    public ClientOut(Socket s){
	this.socket = s;
	try{
	    os = socket.getOutputStream();
	    out = new PrintWriter(os, true);
	}catch(IOException e){
	    e.printStackTrace(); 
	}
	System.out.println("Client Out");
       
    }
    public void run(){

	while(socket.isConnected()){

	}
    }
    /**
     * Envoi un message au serveur
     * @param s Le message a envoyer
     */
    public void send(String s){
	out.flush();
	out.println(s);
    }
    public void deconnexion(){
    	try {
			socket.shutdownOutput();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}


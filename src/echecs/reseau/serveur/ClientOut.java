package echecs.reseau.serveur;

import java.net.*;
import java.io.*;
import java.io.IOException;
/**
 * Classe representant le flux de sortie du client
 *
 */
public class ClientOut extends Thread{
    Socket socket;
    OutputStream os;
    PrintWriter out;
    /**
     * Constructeur de la classe
     * @param s Le socket
     */
    public ClientOut(Socket s){
	this.socket = s;
	try{
	    os = socket.getOutputStream();
	    out = new PrintWriter(os, true);
	}catch(IOException e){
	    e.printStackTrace(); 
	}
	System.out.println("[SERVER] Client Out");
	start();
    }
    public void run(){
    }
    /**
     * Envoi un message au client
     * @param s Le message
     */
    public void send(String s){
		out.flush();
		out.println(s);
    }
}


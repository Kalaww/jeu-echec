package echecs.reseau.serveur;

import java.io.IOException;
import java.net.*;
import java.io.*;
/**
 *Classe representant le flux d'entree du client
 *
 */
public class ClientIn extends Thread{
    Socket socket;
    InputStream in;
    BufferedReader in_reader;
    ServerGameManager gm;
    /**
     * Le constructeur de la classe
     * @param socket Le socket
     * @param gm Le serverGameManager
     */
    public ClientIn(Socket socket, ServerGameManager gm){
    	this.gm = gm;
		this.socket = socket;
		try{
		    in = socket.getInputStream();
		    in_reader = new BufferedReader(new InputStreamReader(in));
		}catch(IOException e){
		    e.printStackTrace();
		}
		start();

    }
    public void run(){
		String s;
		try{
		    while((s = in_reader.readLine()) != null){
		    		gm.recu(s, this.socket);
		  	 } 
		    if(socket.isConnected()){
		    	System.out.println("deconnexion");
			    gm.enleverClient(this.socket);
			    gm.sendAll("k:true");
		    }
		}catch(IOException io){
			System.out.println("deconnexion");
		    gm.enleverClient(this.socket);
		    gm.sendAll("k:true");
		}
    }

}

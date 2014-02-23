package echecs.reseau.client;

import java.io.IOException;
import java.net.*;
import java.io.*;
/**
 * Classe ClientIn, gere le flux entrant
 * @author Thomas
 *
 */
public class ClientIn extends Thread{
    Socket socket;
    InputStream in;
    BufferedReader in_reader;
    Client client;
    /**
     * Constructeur de la classe
     * @param socket Le socket
     * @param c Le client
     */
    public ClientIn(Socket socket, Client c){
		this.socket = socket;
		this.client = c;
		try{
		    in = socket.getInputStream();
		    in_reader = new BufferedReader(new InputStreamReader(in));
		}catch(IOException e){
		    e.printStackTrace();
		}
	
    }
    public void run(){
		String s;
		try{
		    while((s = in_reader.readLine()) != null){
			System.out.println("Recu : "+ s); 
			this.client.recu(s);
		    }
		}catch(IOException io){
		    this.client.timeout();
		}
    }

}

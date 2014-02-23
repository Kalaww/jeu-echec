package echecs.reseau.client;

import java.net.*;
import java.io.*;
/**
 * Classe principale du client
 *
 */
public class Client extends Thread{
	String host = "127.0.0.1";
	int port = 1337;
	JoueurClient jClient;
	ClientIn cin;
	ClientOut cout;
	Socket socket;
	/**
	 * Constructeur de la classe
	 */
    public Client(){
    	start();
    }
    /**
     * Constructeur de la classe
     * @param ip L'IP du serveur à rejoindre
     */
    public Client(String ip){
    	this.host = ip;
    	start();
    }
    public void run(){
	try{
		socket = new Socket(host, port);
	    cin = new ClientIn(socket, this);
	    cin.start();
	    cout = new ClientOut(socket);
	    cout.start();
	    jClient = new JoueurClient(this);
	}
	catch(UnknownHostException e){
	    System.out.println("[ERREUR] Serveur indisponible");
	    System.exit(-1);
	}
	catch(ConnectException e){
		System.out.println("[ERREUR] Connexion refusee");
		System.exit(-1);
	}
	catch(IOException e){
	    e.printStackTrace();
	    System.exit(-1);
	}
}
    /**
     * Permet d'envoyer un message au serveur
     * @param s Le message a envoyer
     */
    public void send(String s){
    	this.cout.send(s);
    }
    /**
     * Gere les message entrant
     * @param s Le message entrant
     */
    public void recu(String s){
    	this.jClient.recu(s);
    }
    /**
     * Retourne l'objet JoueurClient
     * @return Le JoueurClient
     */
    public JoueurClient getJC(){
    	return this.jClient;
    }
    /**
     * Permet de lever un timeout
     */
    public void timeout(){
    	this.jClient.timeout();
    }
    public ClientIn getClientIn(){
    	return this.cin;
    }
    public ClientOut getClientOut(){
    	return this.cout;
    }
    public void deconnexion(){
    	try {
    		
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}

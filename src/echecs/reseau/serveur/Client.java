package echecs.reseau.serveur;

import java.net.*;
/**
 * Classe representant un Client reseau
 *
 */
public class Client{
    Socket socket;
    ClientOut cout;
    ClientIn cin;
    int id;
    /**
     * Constructeur de la classe
     * @param s Le socket
     * @param gm Le ServerGameManager correspondant à la partie en cours
     */
    public Client(Socket s, ServerGameManager gm){
    	this.socket = s;
    	System.out.println("Nouveau client");
    	this.cout = new ClientOut(this.socket);
    	this.cin = new ClientIn(this.socket, gm);
    }
    /**
     * Retourne le socket
     * @return Le socket
     */
    public Socket getSocket(){
    	return this.socket;
    }
    /**
     * Envoi un message au client
     * @param s Le message
     */
    public void send(String s){
        this.cout.send(s);
    }
}

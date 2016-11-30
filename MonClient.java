/**
* @author DI SANTOLO Richard
* Programme pour établir une connection client/serveur, ce code gère le code client.
*/

import java.io.*;
import java.net.*;

public class MonClient {
	private Socket client = null;
	private BufferedReader entree;
	private PrintWriter sortie;
	/** Le constructeur qui indique sur quel serveur se connecter et quel port solliciter
	  * @param hostname le nom du serveur
	  * @param port le numero de port
	  *	@throws IOException si erreur de connexion
	  */
	public MonClient(String hostName, int port) throws IOException {
		try {
		// Convertir la chaine de caracteres "hostName" en une adresse IP valide du serveur
			InetAddress adresseIP = InetAddress.getByName(hostName);
		//creer le Socket vers le serveur
			client = new Socket(adresseIP, port);
		//on fixe un timeOut
			client.setSoTimeout(1000);// 1s
		}
		catch(UnknownHostException e){
			System.err.println("Je ne connais pas le serveur: "+hostName);
			throw e;
		}
		catch(SocketException e){
			System.err.println("erreur de connexion ou timeout");
			throw e;
		}
		catch (IOException e) {
			System.err.println("Probleme de connexion sur:"+hostName);
			throw e;
		}
		System.out.println("Connexion OK sur "+hostName);

		try {
			entree = new BufferedReader(new InputStreamReader(client.getInputStream()) );
			sortie = new PrintWriter(client.getOutputStream() );
		}
		catch (IOException e) {
            	System.err.println("PB création des streams");
           		System.exit(1);
        	}

	}

	/** lit les caractères envoyés par le serveur.
		* @return un objet String qui contient l'ensemble des caractères lus
		*/
	public String lireServeur(){
		String ligne=null;
		try{
		 	ligne = entree.readLine();
		}
		catch (IOException e) {
			System.err.println("rien a lire");
		}
		return ligne;
	}

	/** Envoie des données au serveur.
		* @param ligne les caractères à envoyer
		*/
	public void ecrireServeur(String ligne){
		sortie.println(ligne);
		sortie.flush();
	}
	
	/** teste la connexion.
		*@return un booléen notifiant l'état de la connexion
		*/
	public boolean estConnect(){
		return client.isConnected();
	}
	
	/** Fermeture du socket.
		*/
	public void fermer(){
		try{
			entree.close();
			sortie.close();
			if (client != null)
				client.close();
			System.out.println ("Fermeture ok");
		}
		catch(IOException e){
			System.err.println("Erreur à la fermeture des flux !");
		}
	}

	protected void finalize(){
		fermer();
	}

	public static void main(String[] args){
	// Pour tester un protocole simple de communication
	// Bien entendu il faut que serveur et client soient compatibles

	// initialisation du client
	// on peut passer server et port par la ligne de commande

		MonClient  mc=null; //initialisation de la variable locale
		try{
			if (args.length >1) 
				mc = new MonClient(args[0],Integer.parseInt(args[1]));
			else
				mc = new MonClient("localhost", 2222);
			System.out.println("mc = "+mc+ "client:"+mc.client);
		}
		catch(IOException e) {
			System.err.println("erreur de realisation du client"+mc);
			if (mc != null){
				mc.fermer();
				mc = null; //on perd l'objet
			}
		}
		
		if (mc != null) {
	// Que dit le serveur ?
			String ligne = mc.lireServeur();
			System.out.println("Serveur :"+ligne);
	//Requête au serveur a partir de la console
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			String entree;
			try{
				while ((entree = console.readLine()) != null) {
					mc.ecrireServeur(entree);
					ligne = mc.lireServeur();
					System.out.println("Serveur :"+ligne);// pour voir
					if (ligne.equals("Au revoir"))
						break;
				}

				console.close();
			
			}
			catch(IOException e){
				System.err.println("Erreur de lecture console");
			}
		
			mc.fermer();
		}
		else
			System.err.println("Impossible de créer le client, mc == null !!");
	}//Fin du main

}
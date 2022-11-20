/*
 * Created on Nov 7, 2004
 */

import java.util.*;
import java.io.*;
import java.net.*;

public class YahooBuddyServer implements ConstantValues {

	//instance variable
    private Hashtable online_Clients ; 
	
    //constructor
	public YahooBuddyServer() {
		
		online_Clients = new Hashtable();
	}
/*********************************************************************/	
	public void startServer(){
		
		System.out.println();
		responseToCommands();
		responseToNetRequests();
				
	}
/*********************************************************************/
/*This method runs server socket and listen to incomming client connections.
 * for each connection creates an instance of ServerNetRequest thread
 */	
	private void responseToNetRequests(){
		
      ServerSocket server;
      Socket socket;
      Thread NetRequest;
      try {
		   server = new ServerSocket( SERVER_PORT);
		   System.out.print( "Yahoo server is starting on ");
		   System.out.println("local port " + server.getLocalPort() + "...");
		   System.out.print(">");
           while(true) {
           	//It should not be in a thread too? yes or responseToCommands() must call first
             socket = server.accept();
             System.out.println("\nNew connection established." + " IP : " + socket.getInetAddress().getHostAddress() );
             System.out.print(">");
             ServerNetRequest nr = new ServerNetRequest( online_Clients , socket);
             nr.start();
          }
	  }
      catch(IOException ioe ){
           System.out.println( ioe.getMessage() );
      }	
	}
/*********************************************************************/
// This method creates ServerUserCommands thread for response to user commands.
  	
	private void responseToCommands(){
		
		Thread  uc = new ServerUserCommands( online_Clients );
		uc.start();
	}
	
/*********************************************************************/	
	public static void main(String[] args) {
		
		YahooBuddyServer ybs = new YahooBuddyServer();
		ybs.startServer();
	}
}

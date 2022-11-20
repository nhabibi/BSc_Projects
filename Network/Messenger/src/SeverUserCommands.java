/*
 * Created on Nov 7, 2004
 *
 */
//A class for handle user commands

import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;

class ServerUserCommands extends Thread {
	
	//instance variables
	private Hashtable online_Clients;
	private Enumeration IDs;
	
    //constructor
	public ServerUserCommands(Hashtable online_Clients){
		
		this.online_Clients = online_Clients;
	}
/*************************************************************************/	
	public void run() {
		
		String command = "";
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
				
		while (true) {
			
			try {
				command = br.readLine();
			    if ( command == null )
			    	return;
				if (command.equals("!!!show_buddies")){
			
			    	show_Buddies();
			    	System.out.print(">");
			    }
				
			    else if (command.equals("!!!quit")){
				  
				    System.out.println("Server will be shutting down...");
				    System.exit(0);
		    	}
				
			    else {
			        System.out.println("usage : !!!show_buddies  or !!!quit");
			        System.out.print(">");
			    }//usage    
		   }//try
		   catch(IOException ioe){
				    System.out.println( ioe.getMessage());
					System.out.print(">");
		   }	
	    }
	}		
/************************************************************************/
    private void show_Buddies() {
    	
		if ( online_Clients.isEmpty())
			System.out.println( "No buddy is online." );
	    else {
			IDs = online_Clients.keys();
			while( IDs.hasMoreElements())
				System.out.println( IDs.nextElement());
	    }		
			
   }
 }				

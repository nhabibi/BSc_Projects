
//class for response to clinet request over network
//We defined below protocol between clients and server :

/* application layer protocol between server and clients :
 * 1- register : "register\nID\n"  --> "ok"  or  "duplicate ID,request rejected."
 * 2- query show_buddies  : "show_buddies\n"   -->  "size\n buddy_name1:IP:Port \n
 *                buddy_name2:IP:Port \n ..."
 * 3- query get IP of buddy : "buddy_name:name\n"  --> "budy_name:IP:PORT\n"
 *     or "\n" if he is not online.
 */
//server regster client in form : "ID" , "IP:PORT"

import java.net.*;
import java.util.*;
import java.io.*;

class ServerNetRequest extends Thread {
	
	//instance variables
	private Hashtable online_Clients;
	private Socket socket;
	private DataOutputStream out ;
	private BufferedReader in;
	private String ID;
	
	//constructor
	public ServerNetRequest(Hashtable online_Clients , Socket socket){
		
		this.online_Clients = online_Clients;
		this.socket = socket;
	}
/***********************************************************************/    
	public void run() {
		
		String command = "";
		try {
			//in = new DataInputStream( socket.getInputStream() );
			in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			out = new DataOutputStream( socket.getOutputStream() );
			
			
			while(true) {
				command = in.readLine();
								
				if (command.equals("register")){
					
					ID = in.readLine().toUpperCase();
					boolean result = registerClient(); 
					if ( result == false )
						return;
					//if this not be ckecked, this thread removes another ID from hashtable
				}
				else if ( command.equals("show_buddies")){
					showBuddiesToClient();
				}
				else if ( command.startsWith("buddy_name:"))
					writeBuddyInformation( command.substring( command.indexOf(':') + 1));
				else {
					System.out.println("unknown command from client");
				}
			}//while
		}//try
		catch(Exception e){
			//System.out.print( e.getMessage() + " " + ID + "\n>" );
			removeID();
		}
	}
/********************************************************************/
//This method handle "register\nID\n" request from client
	
	private boolean registerClient(){
		
		String response = "";
				
		if ( online_Clients.containsKey( ID ) ) {
			response = "Duplicated ID,request rejected.\n";
		    
		}
		
		else {
		
			String port = String.valueOf( socket.getPort() );
			InetAddress id = socket.getInetAddress();
			String ip = id.getHostAddress();


                        try {
                             if (ip.equals( "127.0.0.1" ) )
                                 ip = InetAddress.getLocalHost().getHostAddress();

                              System.out.print("\n" + ip + "\n>");
      			}
                        catch( UnknownHostException uhe ) {
                              System.out.println( uhe );
                        }

                  
			online_Clients.put(ID , ip + ":" + port );
			response = "OK\n";
			
		}
		
		try {
			out.writeBytes(response);
			out.flush();
			if( response.startsWith("OK"))
				return true;
			else
				return false;
		}
		catch(IOException se ){
			System.out.println(se.getMessage());
			
		}
		return false;
		
	}
/********************************************************************/
//This method handle "show_buddies\n" request from client
	
	 private void showBuddiesToClient(){
		
		Enumeration e = online_Clients.keys();
		int size = online_Clients.size();
		
		try {
			    out.writeBytes( String.valueOf( size-1 ) + "\n" );
			    out.flush();
				while( e.hasMoreElements()){
				  String key = (String) e.nextElement();
				  String value = (String) online_Clients.get( key );
				  if ( ! key.equals(ID))				     	
				     out.writeBytes( key +"  " + value + "\n" );
				  out.flush();
		   
			    }
				//out.writeBytes( "END" );
				out.flush();
		}//try
		catch(IOException ioe){
			System.out.println( ioe.getMessage() );
		}
	}		
/**********************************************************************/
//This method handle "buddy_name:name\n" request from client
	 
	private void writeBuddyInformation(String ID){
		
		try {
			ID = ID.toUpperCase();
			if ( ! online_Clients.containsKey(ID)) {
				out.writeBytes("\n");
				out.flush();
				//System.out.println(ID + " is not online");
		    }
			else {
				out.writeBytes( ID + ":" + online_Clients.get(ID) + "\n");
				
	   		}
		}//try
		catch(IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		
	}
/*********************************************************************/	
    private void removeID() {
			
	  online_Clients.remove( ID );
	}		
	
}//class

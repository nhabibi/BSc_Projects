
//note : if we close a socket,read from it returns null,but if we crash without close the socket,it is : "Connection reset"

import java.io.*;
import java.net.*;

class ReadFromIConnectedTo extends Thread {
	
	private Socket socket;
	private OnlineBuddies  buddies;
	private String ID;
	
	public ReadFromIConnectedTo(String name , OnlineBuddies online_buddies){
	
		this.buddies = online_buddies;
		this.ID = name;
	}

	public void run(){
		
		try{
			
			 String mess;
			 socket = (Socket) buddies.get( ID );
			 BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			 
			 while(true) {
				//first check for exsitence of ID.
				if ( buddies.contains( ID )) {	 	
				   mess = br.readLine();
				   if ( mess != null )
					  System.out.println( ID + " : " + mess );
				   else { //peer is disconnected
					  System.out.println("You have been disconnected from "+ ID );
					  buddies.disconnect( ID );
					  buddies.removeBuddy( ID );
					  return;
				   }		 
				}//if
				else 
					return;
			}//while
		}
		catch(IOException ioe){
			if ( ioe.getMessage().startsWith("Connection reset") ) {
					System.out.println("You have been disconnected from "+ ID );
					buddies.disconnect( ID );
					buddies.removeBuddy( ID );
			}
		
		}
	}
}


//A class that handle a client requests that connected to it

import java.net.*;
import java.io.*;

class BuddyHandler extends Thread {

	//instance variable
	private Socket socket;
	private OnlineBuddies buddies;
	private String buddy_ID;
	private String my_ID;
	
	//contructor
	public BuddyHandler(Socket socket , OnlineBuddies buddies , String my_ID) {
		
		this.socket = socket;
		this.buddies = buddies;
		this.my_ID = my_ID;
	}
/******************************************************************/	
	public void run() {
		
		try {
			String mess;
			DataOutputStream out = new DataOutputStream( socket.getOutputStream());
			BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			buddy_ID = br.readLine();
			buddy_ID = buddy_ID.toUpperCase();
            buddies.addBuddy( buddy_ID , socket);
			System.out.println("Connection Established with " + buddy_ID);
			//now writes it's ID
			out.writeBytes(my_ID + "\n");
			//now listen!
			while(true) {
				mess = br.readLine();
				if ( mess != null )
					System.out.println( buddy_ID + " : " + mess );
				else { //peer is disconnected
					 System.out.println("You have been disconnected from "+ buddy_ID );
					 buddies.disconnect( buddy_ID );
					 buddies.removeBuddy( buddy_ID );
					 return;
				}		 
				
			}

		}
		catch(IOException ioe){
			//ioe.printStackTrace();
			//System.out.println( ioe.getMessage() + " in Handler"); //It must be removed
			if ( ioe.getMessage().startsWith("Connection reset") ) {
				System.out.println("You have been disconnected from "+ buddy_ID );
				buddies.disconnect( buddy_ID );
				buddies.removeBuddy( buddy_ID );
			}
		}
		//System.out.println("run method exit");
	}

}

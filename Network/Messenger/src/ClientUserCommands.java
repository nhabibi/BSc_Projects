
//A thread for handling user commands in client

import java.io.*;
import java.net.*;
import java.util.Enumeration;

class ClientUserCommands implements ConstantValues {

	//instance variables
	private BufferedReader br ;
	private OnlineBuddies online_buddies;
	private YahooChat yahooChat;
	
	//contructor
	public ClientUserCommands(YahooChat yahooChat){
		
		this.yahooChat = yahooChat;
		online_buddies = yahooChat.getOnlineBuddies();
		start();
	}
/************************************************************************/
	private void start(){
		
		try {
			  br= new BufferedReader( new InputStreamReader(System.in));
			  while (true){
			 	
				String argument;
				String input = br.readLine();
				if ( input == null)
					return;
				if ( input.equals("!!!show_buddies") ){
					showBuddies();
				}
				else if( input.equals("!!!disconnect_all") ) {
					disconnectFromAll();
				}
				else if( input.equals("!!!quit") ) {
					quit();
				}
				else if (input.startsWith("!!!start_chat") ){
					
					   if ((input.length() > 13) && (input.charAt(13) == ' ' || input.charAt(13) == '	'))
					 	
					      startChat( input.substring(13).trim() );
					   else
					 	  maybeSendToAll(input);
				}	
				else if (input.startsWith("!!!disconnect") ) {
					
					   if ((input.length() > 13) && (input.charAt(13) == ' ' || input.charAt(13) == '	'))
					
				          disconnect( input.substring(13).trim());
					   else
					   	   maybeSendToAll(input);
				}  
			    else if (input.startsWith("!!!")) {//format--> name : mess
			            if ( input.indexOf(':') != -1 ) { //because it may be an incorrect command!
			       	        String ID = input.substring(3 , input.indexOf(':')).trim() ;
			       	        String mess = input.substring( input.indexOf(':') + 1);
			        	    sendMessage(ID , mess);
			            }
			            else
			            	maybeSendToAll(input);
			    }    	
			    else {
			      	
			         maybeSendToAll(input);
			    }
			  }//while		
		}//try
		catch(IOException ioe){
			System.out.println( ioe );
		}
  }
 /*****************************************************************/
	private void showBuddies(){
	
		if ( yahooChat.isWithServer() == false){
			
			System.out.println("Not connected to a registration server");
			return;
		}
		try {
			 yahooChat.getServerConnection().writeToServer("show_buddies\n");
			 String size = yahooChat.getServerConnection().readFromServer();
			 int onlines = Integer.parseInt( size );
			 if ( onlines == 0 ) 
			 	System.out.println("There are no buddies online.");
			 else
			while (  onlines-->0  ){
			      String nextBuddy = yahooChat.getServerConnection().readFromServer();		 	
			      System.out.println( nextBuddy.substring(0 , nextBuddy.lastIndexOf(":")));
			}//while      
		}//try
		catch(IOException ioe) {
			//System.out.println(ioe.getMessage() + "   1");
			if ( ioe.getMessage().equals("Connection reset by peer: socket write error") ) {
				yahooChat.setWithServer(false);
				System.out.println("Not connected to a registration server");
			}
		}
	}
/*****************************************************************/
   private void quit(){
		
		//need to clean up any thing?
		System.out.println("Chat application will be exit...");
		System.exit(0);
	}
/*****************************************************************/   
	private void disconnectFromAll(){
		
		if ( online_buddies.size() == 0 ){
			System.out.println("You are not connected to any buddies.");
			return;
		}
		Enumeration IDs = online_buddies.getIDs();
		while ( IDs.hasMoreElements()) 
			
			disconnect((String) IDs.nextElement() );
	}
	
/*****************************************************************/
	private void disconnect(String name){
		
		name = name.toUpperCase();
		if ( ! online_buddies.contains( name ) ){
			System.out.println("You are not connected to " + name );
			return;
		}
		//online_buddies.writeToBuddy(name , "END");
	    online_buddies.disconnect( name );
		online_buddies.removeBuddy( name );
		System.out.println("You have been disconnected from " + name);
	   
	    /*catch(IOException ioe){
	    	System.out.println (ioe.getMessage() );
	    }*/
	}
/*****************************************************************/
	private void startChat(String name){
		
	    //name is a ID or IP.we assume that default is IP.
		name = name.toUpperCase();
		InetAddress IPAddress;
		Socket socket = null;
		byte[] IP = parseIP( name );
		try {
			    //it is maybe a valid IP of a online buddy
				IPAddress = InetAddress.getByAddress( IP );
				socket = new Socket(IPAddress ,CLIENT_PORT);
		}//try
		catch(Exception e){//then it is an ID
		   
			   if( online_buddies.contains(name)) {
			   	  System.out.println("You already connected to "+ name);
			   	  return;
			   }	  
			   try { //then we must fetch from server if we have server!
			   	  if( yahooChat.isWithServer() == true ) {  
			         yahooChat.getServerConnection().writeToServer("buddy_name:" + name + "\n");
			   	     String result = yahooChat.getServerConnection().readFromServer();
			   	     /*if ( result ==  null ){
						yahooChat.setWithServer(false);
						System.out.println("Could not find your buddy.");
						return;
			   	     }
			   	     else*/
				     if ( result.equals("")) { //name is not online
			   	   	    System.out.println("Could not find your buddy.");
			   	   	    return;
			   	     }	  
			   	     else {//name is online
			   	   	    String stringIP = result.substring(result.indexOf(':')+1 , result.lastIndexOf(':'));
			   	   	    byte[] ip = parseIP( stringIP );
					    IPAddress = InetAddress.getByAddress( ip );
					    socket = new Socket(IPAddress , CLIENT_PORT);
			   	     }
			   	 }//if we have server
			   	 else {
			   	 	 System.out.println("Not connected to a registration server");
			   	     return;
			   	 }//else    
			   }//try
			   catch(UnknownHostException uhe){
				   System.out.println( uhe.getMessage() + "\nCould not find your buddy." );
				   return;
			   }
			   catch(IOException ioe){
			   	
				 if ( ioe.getMessage().equals("Connection reset by peer: socket write error") ) {
					yahooChat.setWithServer(false);
					System.out.println("Could not find your buddy.");
				 }
				 else
				    System.out.println( ioe.getMessage() + "\nCould not connect to " + name );
				 return;
			  }//catch
		       	   
		}//outer catch
		
	    try {
		 
		 BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream()));
		 DataOutputStream out = new DataOutputStream( socket.getOutputStream());
		 out.writeBytes(yahooChat.getID() + "\n");
		 String buddy_ID = br.readLine();
		 buddy_ID = buddy_ID.toUpperCase();
	     System.out.println("Connection Established with " + buddy_ID );
	     online_buddies.addBuddy(buddy_ID , socket );
    	 //now make a thread for read from connected peer!
	     new ReadFromIConnectedTo( buddy_ID , online_buddies ).start();
		}//try
		catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
				
	}
/*****************************************************************/
	private void sendMessage(String ID , String mess) {
	
		ID = ID.toUpperCase();
		if ( ! online_buddies.contains( ID ))
			System.out.println( ID + " not found.");
		else
		    online_buddies.writeToBuddy( ID , mess + "\n" );
			
	}
/****************************************************************/
//This method send input any connected client.If no one is connected says: Command not recognized.
	
	private void maybeSendToAll(String input){
		
		if ( online_buddies.size() == 0 ) {
			System.out.println("Command not recognized.");
			return;
		}
		Enumeration e = online_buddies.getIDs();
		while ( e.hasMoreElements())
			online_buddies.writeToBuddy((String)e.nextElement() , input + "\n");
			
	}
/*****************************************************************/
//This method change IP represented in string into an byte[4] array if it is legal.Otherwise returns null
	
  private byte[] parseIP(String ip){
		
		byte[] IP = new byte[4];
		try {
			for(int i =0 ; i <3; ++i) {
				IP[i] = (byte) (Integer.valueOf( ip.substring(0 , ip.indexOf(".")))) .intValue();
				//System.out.println( IP[i]);
				ip = ip.substring( ip.indexOf(".") + 1);
			}
			IP[3] = (byte) (Integer.valueOf(ip)) .intValue();
			//System.out.println( IP[3]);
		}	
		catch(Exception e) {
			return null;
		}
		return IP;
	}
		
	
}//class
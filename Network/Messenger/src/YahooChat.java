
import java.io.IOException;
import java.net.*;

public class YahooChat {

	//instance variable
	private boolean withServer;
	private String ID;
	private String serverIP;
	private ServerConnection sc;
	private OnlineBuddies onlines;
	
	//constructor
	public YahooChat(String[] args) {
		
		onlines = new OnlineBuddies();
		processCommandArguments(args);
		start();
	}
	
/****************************************************************/	
	private void processCommandArguments(String[] args) {
		
		System.out.println();
		if ( args.length <1 ){ //error
			
			System.out.println("Usage: YahooChat \"your_buddy_name\" " +
							   " [Registration server IP address]") ;
			System.exit( 1 );
		}
		else if ( args.length >= 2 ){ //work with a server
			
			setWithServer( true );
			setServerIP( args[1] );
		}
		else {//without server
			 setWithServer( false );
		}
		//set buddy_name
		if ( args[0].length() >50 ){
				System.out.println("Enter a buddy_name less than 50 character");
				System.exit( 1 );
		}	 
		setID( args[0] ); 
   }
	
/****************************************************************/	
   private void start() {
		
		   if ( isWithServer() == false) 
			   System.out.println("Registration server not found, but chatting may continue.");
		   else 
			   registerWithServer();
		   new ListenToBuddies(getID() , onlines).start();
		   responseToUserCommands();
			
	   }
/****************************************************************/	
		private void registerWithServer() {
		
			String result = "";
			try {
				sc = new ServerConnection( getServerIP() );
				System.out.println("Connection with server opened succecssfully.");
			}
			catch(Exception e) {
				if ( (e instanceof UnknownHostException )||(e instanceof SocketException ) ) {
					
				   System.out.println(e.getMessage() + "\n" + " Registration server not found," +
				   " but chatting may continue.");
				   setWithServer(false);
				   return;
				}   
				else if (e instanceof IOException ){
					
					System.out.println(e.getMessage() + "\n" + " Can not open stream from server" +
					"socket.Then server will not be used.");
					setWithServer(false);
					return;
				}
			}//catch	
					
			try{		
				sc.writeToServer("register\n" + ID + "\n");
				result = sc.readFromServer();
				if ( result.equals("Duplicated ID,request rejected.") ){
				
					System.out.println("Another buddy is already registered" +
					" with same name. Please try again.");
					System.exit( 1 );
				}
				System.out.println("Register " +result);
			
			}
			catch(IOException ioe){
		
				 System.out.println( ioe.getMessage() + "\n"  );
			}
		}	
   
/***************************************************************/
	private void responseToUserCommands() {
    	
		new ClientUserCommands( this );
	}
    	
		
/*****************************************************************/	
//	accessor methods
	  public void setWithServer(boolean mode){
		
		  withServer = mode;
	  }
	  public boolean isWithServer(){
		
		  return withServer;
	  }
	  private void setID(String id){
		
		  ID = id;
	  }
	  public String getID() {
		
		  return ID;
	  }
	  private void setServerIP(String ip){
		
		  serverIP = ip;
	  }	
	  public String getServerIP() {
		
		  return serverIP;
	  }		
	  public ServerConnection getServerConnection() {
	  	
		  return sc;
	  }
	  public OnlineBuddies getOnlineBuddies(){
	  	
		return onlines;
	  }
/****************************************************************/
	
	public static void main(String[] args) {
		new YahooChat( args );
	}
}//class

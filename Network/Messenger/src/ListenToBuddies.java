
//A thread that accepts clients connection request

import java.net.*;

class ListenToBuddies extends Thread implements ConstantValues {
	
	//instance variable
	private OnlineBuddies online_buddies;
	private String my_ID;
	
	//contructor
	public ListenToBuddies(String my_ID ,OnlineBuddies ob){
		
		online_buddies = ob;
		this.my_ID = my_ID;
		
	}
/**************************************************************************/	
	public void run() {
		
		ServerSocket ss;
		Socket socket;
		try{
			 ss = new ServerSocket(CLIENT_PORT);
			 System.out.println("Server started on " + ss.getInetAddress().getHostAddress()
			 + " : " + ss.getLocalPort());
			 System.out.println("********************************************");
			 while(true){
		     	
				socket = ss.accept();
				new BuddyHandler(socket , online_buddies ,my_ID ).start();
			 }
	   }//try
	   catch(Exception e){
		   System.out.println( e.getMessage() );
		   //System.exit(1);
	   }
	}//run
}

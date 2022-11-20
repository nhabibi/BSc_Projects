
import java.net.*;
import java.io.*;


class ServerConnection implements ConstantValues {
   
	private DataOutputStream out;
	private BufferedReader in;
	private Socket socket;
	
	public ServerConnection(String IP) throws Exception {
		
		createConnection( IP );
	}
	
	private void createConnection(String ip) throws Exception {
		//may exists a method in lib for this!
		//bad digit must be handle
		byte[] IP = parseIP(ip);
		
		try {
			 if ( IP == null ) //ip may be domain name
			     socket = new Socket(  InetAddress.getByName(ip) , SERVER_PORT );
			 else
		         socket = new Socket(  InetAddress.getByAddress(IP) , SERVER_PORT );
		     in  = new BufferedReader ( new InputStreamReader( socket.getInputStream()));
		     out = new DataOutputStream (socket.getOutputStream());
	    }
		catch(UnknownHostException uhe){
			
			throw uhe;
		}
		catch (SocketException se){ //if socket creation fails:
			
			throw se;
		}
		catch(IOException ioe) { //error in get in and out
			System.out.println(ioe.getMessage());
			throw ioe;
		}//if this occurs, should we do?in and out are null
		
  } 
/************************************************************************/		
	public String readFromServer() throws IOException {
		
		return  in.readLine(); 
	}
/***********************************************************************/
    public void writeToServer(String mess) throws IOException {
    	
    	out.writeBytes( mess );
    	out.flush();
    }
/**********************************************************************/
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

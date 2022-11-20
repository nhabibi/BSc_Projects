import java.net.MalformedURLException;
import java.rmi.*;

public class Client {

	public int[] connect(String ip, int[] first, int[] second){

		int[] result = null;
		
		System.setSecurityManager( new LaxSecurityManager() );
		try{
			Add2Arrays remote = (Add2Arrays)Naming.lookup("rmi://" + ip + "/sharedObject");
			result = remote.add2Arrays(first, second);
			for (int i=0; i< result.length; ++i) {
				
				System.out.print(result[i] + "  ");
			}
			System.out.println();
			
		}
		catch(MalformedURLException mue){
			System.out.println("1: " + mue.getMessage());
		}
		catch(NotBoundException nbe){
			System.out.println("2: " + nbe.getMessage());
		}
		catch(RemoteException re){
			System.out.println("3: " + re.getMessage());
		}
		return result;
	}

}

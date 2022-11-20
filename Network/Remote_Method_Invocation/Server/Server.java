import java.rmi.*;
import java.net.MalformedURLException;

public class Server {

	public void start(){
		
		//Execute rmiregistry here.
	    try {
			 java.rmi.registry.LocateRegistry.createRegistry(1099);
			 System.out.println("RMI registry ready.");
	    } 
	    catch (Exception e) {
			 System.out.println("Exception starting RMI registry:");
			 e.printStackTrace();
	    }	

		try{
			Add2ArraysImp reference = new Add2ArraysImp();
			Naming.rebind("sharedObject", reference);
		}
		catch(RemoteException re){
			System.out.println("1: " + re.getMessage());
		}
		catch(MalformedURLException mue){
			System.out.println("2: " + mue.getMessage());
		}
		
	}

}

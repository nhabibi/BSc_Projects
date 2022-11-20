import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;


public class Add2ArraysImp extends UnicastRemoteObject implements Add2Arrays {

	static final long serialVersionUID = 123;
	
	public Add2ArraysImp() throws RemoteException {
		super();
	}

	public Add2ArraysImp(int arg0) throws RemoteException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public Add2ArraysImp(int arg0, RMIClientSocketFactory arg1,
			RMIServerSocketFactory arg2) throws RemoteException {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public int[] add2Arrays(int[] first, int[] second) throws RemoteException {
		
		int length = Math.min(first.length, second.length);
		int[] result = new int[length];
		for (int i=0; i< length; ++i) {
			
			result[i] = first[i] + second[i];
		}
		return result;
	}

}

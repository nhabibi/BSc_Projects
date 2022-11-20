import java.rmi.*;

public interface Add2Arrays extends Remote {

	int[] add2Arrays(int[] first, int[] second)throws RemoteException ;
}

import java.rmi.*;

public interface ChatClientInterface extends Remote {
    public void messageFromServer(String message) throws RemoteException;
}

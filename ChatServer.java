import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
    Vector<Chat> chatters;
    String line = "----------------------------------------\n";

    public ChatServer() throws RemoteException {
        super();
        chatters = new Vector<>(10, 1);
    }

    public static void main(String args[]){
        initialize();
    }

    public static void initialize() {
        try{
            //System.setProperty("java.rmi.server.hostname","10.113.9.182");
            ChatServer server = new ChatServer();
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:5000/chat", server);
            System.out.println("[Server]: Chat Server Initialized");
        } catch(Exception e){
            System.out.println("Chat Server Failed to Start");
        }
    }

    public void connect(String nickname) {
        registerChatter(nickname);
        sendToAll("[Server] : " + nickname + " connected");
    }

    public void sendMessage(String nickname, String message) {
        message = "[" + nickname + "]: " + message;
        sendToAll(message);
    }

    private void registerChatter(String nickname) {
        try {
            ChatClientInterface client = (ChatClientInterface) Naming.lookup("rmi://localhost:5000/"+nickname);
            chatters.addElement(new Chat(nickname, client));
            client.messageFromServer("[Server]: Connected and free to Chat");
        } catch(Exception e) {
            System.out.println("Cannot register " + nickname);
        }
    }

    public void sendToAll(String message) {	
		for(Chat c : chatters){
			try {
				c.getClient().messageFromServer(message);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}

    public void disconnect(String nickname) {
		for(Chat c : chatters){
			if(c.getNickname().equals(nickname)) {
				System.out.println(line + nickname + " left the chat session");
				chatters.remove(c);
				break;
			}
        }
	}
}

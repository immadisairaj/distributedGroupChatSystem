import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class ChatClient extends UnicastRemoteObject implements ChatClientInterface {
    private String nickname;
    protected ChatServerInterface chatInterface;

    public ChatClient(String nickname, ChatServerInterface chatInterface) throws RemoteException {
        super();
        this.nickname = nickname;
        this.chatInterface = chatInterface;
    }

    public static void initialize(ChatClient client) {
        try {
            Naming.rebind("rmi://localhost:5000/" + client.nickname, client);
        } catch(Exception e) {
            System.out.println("Failed initializing client");
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter nickname: ");
        String name = scan.next();
        ChatClient client = null;
        try{
            ChatServerInterface chatInterface = (ChatServerInterface) Naming.lookup("rmi://localhost:5000/chat");
            client = new ChatClient(name, chatInterface);
        } catch(Exception e) {
            System.out.println("Failed initializing client");
        }
        if(client==null) {
            System.out.println("Try to connect again");
            scan.close();
            return;
        }
        initialize(client);
        client.registerWithServer(client.nickname);
        System.out.println("Enter Messages Below. To exit from chat, enter \"exit\"");
        String message = "";
        scan.nextLine();
        while(true) {
            try{
                message = scan.nextLine();
                if(message.equals("exit"))
                    break;
                if(message != "")
                    client.chatInterface.sendMessage(client.nickname, message);
                message = "";
            } catch(Exception e) {
                System.out.println("Failed sending message, try again");
            }
        }
        try{
            client.chatInterface.disconnect(client.nickname);
        } catch(Exception e) {
            System.out.println("Failed to leave chat");
        }
        scan.close();
        System.exit(0);
    }

    public void registerWithServer(String nickname) {
        try {
            chatInterface.connect(nickname);
        } catch(Exception e) {
            System.out.println("Failed connecting " + nickname);
        }
    }

    public void messageFromServer(String message) {
        System.out.println(message);
    }
}
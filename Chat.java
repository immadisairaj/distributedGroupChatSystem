public class Chat {
    String nickname;
    ChatClientInterface clientInterface;

    public Chat(String nickname, ChatClientInterface clientInterface) {
        this.nickname = nickname;
        this.clientInterface = clientInterface;
    }

    public String getNickname() {
        return nickname;
    }

    public ChatClientInterface getClient() {
        return clientInterface;
    }
}

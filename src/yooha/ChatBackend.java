

package yooha;

import java.util.List;


public abstract class ChatBackend
{
    public int chat_id;
    public List<ConnectionData> connectionDatas;
    public Chat chat;

    public abstract void sendMessage(Message message);
    public abstract void shutdown();

    public abstract List<ConnectionData> getConnectionData();

    public void setChat(Chat chat)
    {
        this.chat = chat;
    }
}

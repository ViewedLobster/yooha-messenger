

package yooha;

import yooha.cipher.KeyHandler;
import java.util.List;
import java.io.File;



public abstract class ChatBackend
{
    public int chat_id;
    public List<ConnectionData> connectionDatas;
    public Chat chat;
    public KeyHandler keyHandler;
    public boolean encrypt;

    public abstract void sendMessage(Message message);
    public abstract void shutdown();
    public abstract void sendDisconnect();
    public abstract void setPreferredCipher(String type);
    public abstract void overrideCipher( String type, String keyString );
    public abstract void sendFileRequest( File f, String text );

    public abstract List<ConnectionData> getConnectionData();

    public void setChat(Chat chat)
    {
        this.chat = chat;
    }

    public void enableEncryption( boolean b )
    {
        this.encrypt = b;
    }
}

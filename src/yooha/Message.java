package yooha;
import java.awt.Color;
import java.lang.StringBuilder;

public class Message {
    public final String messageText;
    public final String senderName;
    public final String requestText;
    public final Color color;
    public final boolean disconnect;
    public final boolean request;
    public final boolean requestDenied;
    public final boolean keyRequest;
    public final String keyType;
    public final String key;
    public final String keyReply;
    public final boolean fileRequest;
    public final String fileRequestFileName;
    public final int fileRequestFileSize;
    public final boolean fileResponse;
    public final boolean fileResponseReply;
    public final int fileResponsePort;

    public Message(String senderNameIn, String textIn, Color colorIn, boolean disconnect){
        this.messageText = textIn;
        this.senderName = senderNameIn;
        this.color = colorIn;
        this.disconnect = disconnect;
        this.request = false;
        this.requestText = null;
        this.requestDenied = false;
        this.keyRequest = false;
        this.keyType = null;
        this.key = null;
        this.keyReply = null;
        this.fileRequest = false;
        this.fileRequestFileName = null;
        this.fileRequestFileSize = -1;
        this.fileResponse = false;
        this.fileResponseReply = false;
        this.fileResponsePort = -1;
    }

    public Message(String senderNameIn, String textIn, Color colorIn, boolean disconnect, boolean request, String requestText, boolean requestDenied){
        this.messageText = textIn;
        this.senderName = senderNameIn;
        this.color = colorIn;
        this.disconnect = disconnect;
        this.request = request;
        this.requestText = requestText;
        this.requestDenied = requestDenied;
        this.keyRequest = false;
        this.keyType = null;
        this.key = null;
        this.keyReply = null;
        this.fileRequest = false;
        this.fileRequestFileName = null;
        this.fileRequestFileSize = -1;
        this.fileResponse = false;
        this.fileResponseReply = false;
        this.fileResponsePort = -1;
    }

    public Message( String senderNameIn,
                    String textIn,
                    Color colorIn,
                    boolean disconnect,
                    boolean request,
                    String requestText,
                    boolean requestDenied,
                    boolean keyRequest,
                    String keyType,
                    String key,
                    String keyReply )
    {
        this.messageText = textIn;
        this.senderName = senderNameIn;
        this.color = colorIn;
        this.disconnect = disconnect;
        this.request = request;
        this.requestText = requestText;
        this.requestDenied = requestDenied;
        this.keyRequest = keyRequest;
        this.keyType = keyType;
        this.key = key;
        this.keyReply = keyReply;
        this.fileRequest = false;
        this.fileRequestFileName = null;
        this.fileRequestFileSize = -1;
        this.fileResponse = false;
        this.fileResponseReply = false;
        this.fileResponsePort = -1;
    }

    public Message( String senderNameIn,
                    String textIn,
                    Color colorIn,
                    boolean disconnect,
                    boolean request,
                    String requestText,
                    boolean requestDenied,
                    boolean keyRequest,
                    String keyType,
                    String key,
                    String keyReply,
                    boolean fileRequest,
                    String fileRequestFileName,
                    int fileRequestFileSize, 
                    boolean fileResponse,
                    boolean fileResponseReply,
                    int fileResponsePort
                    )
    {
        this.messageText = textIn;
        this.senderName = senderNameIn;
        this.color = colorIn;
        this.disconnect = disconnect;
        this.request = request;
        this.requestText = requestText;
        this.requestDenied = requestDenied;
        this.keyRequest = keyRequest;
        this.keyType = keyType;
        this.key = key;
        this.keyReply = keyReply;
        this.fileRequest = fileRequest;
        this.fileRequestFileName = fileRequestFileName;
        this.fileRequestFileSize = fileRequestFileSize;
        this.fileResponse = fileResponse;
        this.fileResponseReply = fileResponseReply;
        this.fileResponsePort = fileResponsePort;
    }

    public String getSenderName(){
        return senderName;
    }

    public String getText(){
        return messageText;
    }

    public Color getColor(){
        return color;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Sender: ");
        if (senderName != null)
            sb.append(senderName + "\n");
        else
            sb.append("null\n");

        sb.append("Disconnect: " + disconnect + "\n");
        sb.append("Color: ");
        if (color != null)
            sb.append(color.toString()+"\n");
        else
            sb.append("null\n");

        sb.append("Message text:\n");
        if (messageText != null)
            sb.append(messageText);
        else
            sb.append("null");

        return sb.toString();
    }
    
}

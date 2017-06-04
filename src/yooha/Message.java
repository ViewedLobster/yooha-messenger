package yooha;
import java.awt.Color;
import java.lang.StringBuilder;

public class Message {
    public final String messageText;
    public final String senderName;
    public final Color color;
    public final boolean disconnect;

    public Message(String senderNameIn, String textIn, Color colorIn, boolean disconnect){
        this.messageText = textIn;
        this.senderName = senderNameIn;
        this.color = colorIn;
        this.disconnect = disconnect;
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

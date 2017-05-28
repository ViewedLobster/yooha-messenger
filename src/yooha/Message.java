package yooha;
import java.awt.Color;

public class Message {
    private String messageText;
    private String senderName;
    private Color color;
    private boolean completeMessage;

    public Message(Color colorIn, String textIn, String senderNameIn){
        messageText = textIn;
        senderName = senderNameIn;
        color = colorIn;
        completeMessage = true;
    }

    public Message(Color colorIn, String textIn, String senderNameIn, boolean cmIn){
        messageText = textIn;
        senderName = senderNameIn;
        color = colorIn;
        completeMessage = cmIn;
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
    
    private void setMessageText(String messageTextIn){
        messageText = messageTextIn;
    }

    private void setSenderName(String senderNameIn){
        senderName = senderNameIn;
    }
    private void setColor(Color colorIn){
        color = colorIn;
    }
    private void setCompleteMessage(boolean cm){
        completeMessage = completeMessage;
    }
}

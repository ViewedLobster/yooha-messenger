import java.awt.Color;

public class Message {
    private String messageText;
    private String senderName;
    private Color color;

    public Message(Color colorIn, String textIn, String senderNameIn){
        messageText = textIn;
        senderName = senderNameIn;
        color = colorIn;
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
}

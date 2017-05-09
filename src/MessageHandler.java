import java.awt.Color;
import java.io.*;

import javax.xml.parsers.*;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;


public class MessageHandler extends DefaultHandler {
    private StringBuilder messageText;
    private String sender;
    private Color color;
    private boolean inMessage = false;
    private Message message = null;
    private boolean inEncrypted = false;
    private StringBuilder encryptedPart;
    private boolean messageIsComplete = false;
    private CipherHandler ch;


    public MessageHandler(CipherHandler chIn){
        messageText = new StringBuilder();
        color = null;
        sender = "bajs";
        ch = chIn;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "message":
                sender = attributes.getValue("sender");
                inMessage = true;
                break;
            case "text":
                if (inMessage) {
                    color = Color.decode(attributes.getValue("color"));
                }
                break;
            case "encrypted":
                encryptedPart = new StringBuilder();
                inEncrypted = true;
                break;
            case "bold":
                messageText.append("<b>");
                break;
            case "italics":
                messageText.append("<i>");
                break;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "message": 
                if (inMessage) {
                    inMessage = false;
                    messageIsComplete = true;
                }
                break;
            case "encrypted":
                if (inEncrypted || ch != null) {
                    inEncrypted = false;
                    String encrypted = encryptedPart.toString();
                    String decrypted = ch.decrypt(encrypted);
                    if ( decrypted != null ){
                        try {
                            Message partial = MessageParser.parseMessageXML(decrypted, ch);
                            mergeWithMessage(partial);
                        } catch (Exception e) {
                            System.err.println("Ooops, something went wrong when parsing decrypted content.");
                            messageText.append("[Undecipherable content]");
                        }
                    } else {
                        System.err.println("Something seems to have gone wrong while decrypting some content");
                        messageText.append("[Undecipherable content]");
                    }

                } else {
                    messageText.append("[Undecipherable content]");
                }
                break;
            case "bold":
                if (inMessage) {
                    messageText.append("</b>");
                }
                break;
            case "italics":
                messageText.append("</i>");
                break;
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException{
        if (inEncrypted) {
            encryptedPart.append(new String(ch, start, length));
        } else {
        messageText.append(new String(ch, start, length));
        }
    }

    public void buildMessage(){
        message = new Message(color, messageText.toString(), sender, messageIsComplete);
        color = null;
        messageText.delete(0, messageText.length());
    }
    
    public Message getMessage(){
        Message tempMess = message;
        message = null;
        return tempMess;
    }
    
    public void mergeWithMessage(Message m) {
        // Check what fields are valid and append/set the corresponding field in messagehandler
        if (m.getColor() != null){
            color = m.getColor();
        }
        if (m.getText() != null){
            messageText.append(m.getText());
        }

    }
}


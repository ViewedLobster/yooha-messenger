import java.awt.Color;
import java.io.*;

import javax.xml.parsers.*;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;


/*
 * This class parses an XML string and returns an object of type Message.
 */

public class MessageParser {
    SAXParser parser;
    MessageHandler handler;
    
    public MessageParser() throws ParserConfigurationException, SAXException{
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parser = parserFactory.newSAXParser();
        handler = new MessageHandler();
    }

    public Message parseMessageXML(String XMLString) throws IOException, SAXException{
        parser.parse(new InputSource(new StringReader(XMLString)), handler);
        return handler.getMessage();
    }


    private class MessageHandler extends DefaultHandler {
        private StringBuilder messageText;
        private String sender;
        private Color color;
        private boolean inMessage = false;
        private Message message = null;

        public MessageHandler(){
            messageText = new StringBuilder();
            color = null;
            sender = "bajs";
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
                        buildMessage();
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
            messageText.append(new String(ch, start, length));
        }

        public void buildMessage(){
            message = new Message(color, messageText.toString(), sender);
            color = null;
            messageText.delete(0, messageText.length());
        }
        
        public Message getMessage(){
            Message tempMess = message;
            message = null;
            return tempMess;
        }
    }
}

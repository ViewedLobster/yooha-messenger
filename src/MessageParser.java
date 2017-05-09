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
    static SAXParserFactory saxFactory = SAXParserFactory.newInstance();
    
    public MessageParser() throws ParserConfigurationException, SAXException{
        saxFactory = SAXParserFactory.newInstance();
    }

    static public Message parseMessageXML(String XMLString, CipherHandler ch) throws IOException, SAXException, ParserConfigurationException{
        System.out.println("Parsing now!");
        SAXParser parser = saxFactory.newSAXParser();
        MessageHandler handler = new MessageHandler(ch);
        parser.parse(new InputSource(new StringReader(XMLString)), handler);
        handler.buildMessage();
        return handler.getMessage();
    }

}

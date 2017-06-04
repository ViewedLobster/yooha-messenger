package yooha;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Stack;
import java.awt.Color;

public class MessageParser
{
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static final String[] knownTags = {"message",
                                               "text",
                                               "disconnect",
                                               //"request"
                                                };

    public static boolean allowedTagName( String tagName )
    {
        for (String s : knownTags)
        {
            if ( s.equals(tagName) )
            {
                return true;
            }
        }
        return false;
    }

    public static Element removeUnknown(Element root)
    {
        if ( root.getTagName() != "message" )
        {
            return null;
        }

        Stack<Element> stack = new Stack<Element>();

        stack.push(root);

        while (! stack.isEmpty())
        {
            Element e = stack.pop();
            NodeList nl = e.getChildNodes();

            /* Go through all nodes:
             * Let be if not Element node
             * If element node: Check if tag name is allowed and prune if not
             */
            for (int i = 0; i < nl.getLength(); i++ )
            {
                Node n = nl.item(i);

                if ( n instanceof Element )
                {
                    Element en = (Element)n;
                    if ( allowedTagName(en.getTagName()) )
                    {
                        stack.push(en);
                    }
                    else
                    {
                        e.removeChild(n);
                    }
                }
            }

        }

        return root;

    }

    public static String getNodeString( Node n )
    {
        try 
        {
            TransformerFactory tFact = TransformerFactory.newInstance();
            Transformer t = tFact.newTransformer();
            StringWriter sw = new StringWriter();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(n), new StreamResult(sw));

            String s = sw.toString();

            return s;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;

    }

    public static Message parseString( String s )
    {

        try{
            // Create parsing resources
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream bs = new ByteArrayInputStream( s.getBytes("UTF-8") );
            Document doc = builder.parse(bs);

            // The fields to be filled with data
            String sender;
            boolean disconnect = false;
            String messageText;
            Color textColor;

            // Get root an remove unknown elements from doc tree
            Element root = doc.getDocumentElement();
            root = removeUnknown(root);
            if (root == null)
            {
                return null;
            }
            sender = root.getAttribute("sender");
            
            NodeList nl = root.getElementsByTagName("disconnect");
            if (nl.getLength() > 0)
            {
                disconnect = true;
            }

            nl = root.getElementsByTagName("text");
            if (nl.getLength() > 0) 
            {
                Element e = (Element)nl.item(0);
                
                // Get text color from message
                String colorString = e.getAttribute("color");
                if ( colorString.equals("") )
                    textColor = Color.BLACK;
                else
                    textColor = Color.decode(colorString);

                // Get the text from the message
                messageText = e.getTextContent();
            }
            else
            {
                textColor = null;
                messageText = null;
            }

            return new Message(sender, messageText, textColor, disconnect);

            /*
            NodeList nl = doc.getElementsByTagName("message");
            Node n = nl.item(0);
            if ( n.getNodeType() == Node.ELEMENT_NODE )
            {
                Element nElement = (Element) n;
                Element e1 = (Element)nElement.getElementsByTagName("text").item(0);
                System.out.println(e1.getTextContent());

                String username = nElement.getAttribute("username");
                System.out.println(username);
            }
            */
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;

    }

    public static void main(String[] args) {
        String s = "<message sender=\"rolf\"><disconnect /> <text> this is some text <tag1> in tags </tag1> second line </text> </message>";
        Message m = parseString(s);
        System.out.println(m);
        System.out.println(s);
    }
}

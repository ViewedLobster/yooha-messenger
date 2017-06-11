package yooha;

import yooha.cipher.CipherHandler;

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

    public static final String[] knownTags = {"message", "text",
                                               "disconnect",
                                               "request",
                                               "keyrequest",
                                               "encrypted",
                                               "decrypted",
                                               "filerequest",
                                               "fileresponse",
                                               "kursiv",
                                               "fetstil"
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
        if ( !allowedTagName(root.getTagName()) )
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

    public static Element parseElementString( String s )
    {
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream bs = new ByteArrayInputStream( s.getBytes("UTF-8") );
            Document doc = builder.parse(bs);

            Element root = doc.getDocumentElement();
            return root;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Message parseString( String s, CipherHandler ch )
    {

        try{
            // Create parsing resources
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream bs = new ByteArrayInputStream( s.getBytes("UTF-8") );
            Document doc = builder.parse(bs);

            // The fields to be filled with data
            String sender;
            boolean disconnect = false;
            boolean request = false;
            boolean requestDenied = false;
            String messageText;
            String requestText;
            Color textColor;
            boolean keyRequest = false;
            String keyType = null;
            String key = null;
            String keyReply = null;
            boolean fileRequest = false;
            String fileRequestFileName = null;
            int fileRequestFileSize = -1;
            boolean fileResponse = false;
            boolean fileResponseReply = false;
            int fileResponsePort = -1;

            // Get root an remove unknown elements from doc tree
            Element root = doc.getDocumentElement();
            root = removeUnknown(root);
            if (root == null)
            {
                return null;
            }
            sender = root.getAttribute("sender");

            
            NodeList nl = root.getElementsByTagName("encrypted");
            if ( nl.getLength() > 0 )
            {
                Element e = (Element)nl.item(0);
                String type = e.getAttribute("type");

                if ( ch != null && ch.type.equals(type) )
                {
                    // If we have a cipher handler and it matches the type we decrypt and
                    // replace the "encrypted" element by our decrypted element
                    String ciphertext = e.getTextContent();
                    String plaintext = ch.decryptText(ciphertext);

                    plaintext = "<decrypted type=\"" + ch.type + "\">" + 
                                plaintext + "</decrypted>";
                    Element decrypted = parseElementString(plaintext);
                    decrypted = (Element)doc.importNode(decrypted, true);
                    e.getParentNode().replaceChild( removeUnknown(decrypted), e );
                }

            }
            
            nl = root.getElementsByTagName("disconnect");
            if (nl.getLength() > 0)
            {
                disconnect = true;
            }
            
            nl = root.getElementsByTagName("request");
            if (nl.getLength() > 0)
            {
                request = true;
                Element e = (Element)nl.item(0);

                requestText = e.getTextContent();

                requestDenied = e.getAttribute("reply").equals("no");
            }
            else
            {
                requestText = null;
            }

            nl = root.getElementsByTagName("keyrequest");
            if ( nl.getLength() > 0 )
            {
                keyRequest = true;
                Element e = (Element)nl.item(0);

                String tmp = e.getAttribute("type");
                if (!tmp.equals(""))
                    keyType = tmp;

                tmp = e.getAttribute("key");
                if ( !tmp.equals("") )
                    key = tmp;
                tmp = e.getAttribute("reply");
                if (!tmp.equals(""))
                    keyReply = tmp;
            }

            nl = root.getElementsByTagName("filerequest");
            if (nl.getLength() > 0 )
            {
                fileRequest = true;
                Element e = (Element)nl.item(0);
                fileRequestFileName = e.getAttribute("name");
                fileRequestFileSize = Integer.parseInt(e.getAttribute("size"));
            }

            nl = root.getElementsByTagName("fileresponse");
            if (nl.getLength() > 0)
            {
                fileResponse = true;
                Element e = (Element)nl.item(0);
                fileResponseReply = "yes".equals(e.getAttribute("reply"));
                fileResponsePort = Integer.parseInt(e.getAttribute("port"));
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

            return new Message( sender,
                                messageText,
                                textColor,
                                disconnect,
                                request, 
                                requestText, 
                                requestDenied, 
                                keyRequest, 
                                keyType, 
                                key, 
                                keyReply,
                                fileRequest,
                                fileRequestFileName,
                                fileRequestFileSize,
                                fileResponse,
                                fileResponseReply,
                                fileResponsePort
                                );

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
    }

    public static Message getDisconnectMessage( String nick )
    {
        return new Message(nick, "User disconnected.", Color.RED, true );
    }

    public static Message getRequestDeniedMessage( String nick )
    {
        return new Message("chat_system", "Server did not accept your connection.", Color.RED, false );
    }
}

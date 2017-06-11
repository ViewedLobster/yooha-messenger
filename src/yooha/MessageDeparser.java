package yooha;

import java.io.File;
import java.util.Calendar;

import yooha.cipher.CipherHandler;

public class MessageDeparser {
	
	public MessageDeparser(){
	}
	
	public static String deparseMessage(Message message, CipherHandler ch){
		StringBuilder XMLBuilder = new StringBuilder();
		String messageTextElement = "<text color=\"#" +
                                            Integer.toHexString(message.getColor().getRGB()).substring(2) +
                                            "\">" +
                                            cleanUp(message.getText()) +
                                            "</text>";
                if ( ch != null )
                {
                    messageTextElement = "<encrypted type=\"" +
                                         ch.type + 
                                         "\">" + 
                                         ch.encryptText(messageTextElement) + 
                                         "</encrypted>";
                }
		
		XMLBuilder.append("<message sender=\"");
		XMLBuilder.append(cleanUp(message.getSenderName()));
		XMLBuilder.append("\">");
		XMLBuilder.append(messageTextElement);
		XMLBuilder.append("</message>");
		
		return XMLBuilder.toString();
	}
	
	public static String cleanUp(String textIn){
		StringBuilder textBuilder = new StringBuilder();
		
		for(int i=0; i<textIn.length(); i++){
			String symbol = textIn.substring(i,i+1);
			
			switch(symbol){
			case "<": symbol = "&lt;";
			break;
			case ">": symbol = "&gt;";
			break;
			case "&": symbol = "&amp;";
			break;
			case "'": symbol = "&apos;";
			break;
			case "\"": symbol = "&quot;";
			default:
				break;
			}
			textBuilder.append(symbol);
		}
		return textBuilder.toString();
	}
	
	public static String deparseToHTML(Message message){
		Calendar calendar = Calendar.getInstance();
		StringBuilder HTMLBuilder = new StringBuilder();
		
		HTMLBuilder.append("<p>");
		HTMLBuilder.append(cleanUp(message.getSenderName()));
		HTMLBuilder.append(" (");
		HTMLBuilder.append(calendar.getTime());
		HTMLBuilder.append("):<br>");
		HTMLBuilder.append("<font style=\"color:");
		HTMLBuilder.append(Integer.toHexString(message.getColor().getRGB()).substring(2));
		HTMLBuilder.append("\">");
		HTMLBuilder.append(cleanUp(message.getText()).replaceAll("\n", "<br>"));
		HTMLBuilder.append("</font></p>");
		
		return HTMLBuilder.toString();
	}

        public static String getDisconnectXml( String nick )
        {
            StringBuilder sb = new StringBuilder();

            sb.append("<message sender=\"");
            sb.append(cleanUp(nick));
            sb.append("\"><disconnect /></message>");
            return sb.toString();

        }

        public static String getRequestRejection()
        {
            return "<message sender=\"chat_server\"><request reply=\"no\"></request></message>";
        }
        
        public static String getSimpleRejection()
        {
            return "<message sender=\"chat_server\"><text color=\"#FF0000\">You are not allowed to join chat. Disconnect.</text></message>";
        }

        public static String getRequestString(String nick)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("<message sender=\"");
            sb.append(cleanUp(nick));
            sb.append("\"><request>Halloj! Jag vill chatta med dej!</request></message>");
            return sb.toString();
        }

        public static String getKeyRequestString(String nick, String type, String keyString )
        {
            StringBuilder sb = new StringBuilder();
            sb.append("<message sender=\"");
            sb.append(cleanUp(nick));
            sb.append("\"><keyrequest type=\"");
            sb.append( type );
            sb.append("\" key=\"");
            sb.append(keyString);
            sb.append("\"></keyrequest></message>");
            return sb.toString();
        }

        public static String getKeyRequestReplyString(String nick, boolean supported, String type, String keyString )
        {
            StringBuilder sb = new StringBuilder();
            sb.append("<message sender=\"");
            sb.append(cleanUp(nick));
            sb.append("\"><keyrequest type=\"");
            sb.append( type );
            sb.append("\" reply=\"");
            if ( supported )
            {
                sb.append("supported");
                sb.append("\" key=\"");
                sb.append(keyString);
            }
            else
                sb.append("not_supported");
            sb.append("\"></keyrequest></message>");
            return sb.toString();
        }

        public static String getFileRequestString( String nick, File f, String text )
        {
            if ( f.length() > Integer.MAX_VALUE )
                return null;
            StringBuilder sb = new StringBuilder();
            sb.append("<message sender=\"");
            sb.append(cleanUp(nick));
            sb.append("\"><filerequest name=\"");
            sb.append( f.getName() );
            sb.append( "\" size=\"");
            sb.append(f.length());
            sb.append("\"><text color=\"#000000\">");
            sb.append(text);
            sb.append("</text></filerequest></message>");
            return sb.toString();

        }

        public static String getFileResponseString( String nick, String messageText, boolean reply, int port )
        {
            StringBuilder sb = new StringBuilder();
            sb.append("<message sender=\"");
            sb.append(cleanUp(nick));
            sb.append("\"><fileresponse reply=\"");
            if ( reply )
                sb.append("yes");
            else
                sb.append("no");
            sb.append( "\" port=\"");
            sb.append(port);
            sb.append("\"><text color=\"#000000\">");
            sb.append(messageText);
            sb.append("</text></fileresponse></message>");
            return sb.toString();
        }
}

import java.util.Calendar;

public class MessageDeparser {
	
	public MessageDeparser(){
	}
	
	public String deparseMessage(Message message){
		StringBuilder XMLBuilder = new StringBuilder();
		String textIn = message.getText();
		
		XMLBuilder.append("<message sender=\"");
		XMLBuilder.append(cleanUp(message.getSenderName()));
		XMLBuilder.append("\"><text color=\"#");
		XMLBuilder.append(Integer.toHexString(message.getColor().getRGB()).substring(2));
		XMLBuilder.append("\">");
		XMLBuilder.append(cleanUp(message.getText()));
		XMLBuilder.append("</text></message>");
		
		return XMLBuilder.toString();
	}
	
	public String cleanUp(String textIn){
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
	
	public String deparseToHTML(Message message){
		Calendar calendar = Calendar.getInstance();
		StringBuilder HTMLBuilder = new StringBuilder();
		
		HTMLBuilder.append("<p>");
		HTMLBuilder.append(message.getSenderName());
		HTMLBuilder.append(" (");
		HTMLBuilder.append(calendar.getTime());
		HTMLBuilder.append("):<br>");
		HTMLBuilder.append("<font style=\"color:");
		HTMLBuilder.append(Integer.toHexString(message.getColor().getRGB()).substring(2));
		HTMLBuilder.append("\">");
		HTMLBuilder.append(message.getText());
		HTMLBuilder.append("</font></p>");
		
		return HTMLBuilder.toString();
	}
}

import java.awt.*;
import javax.swing.text.html.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatView extends JPanel implements ActionListener{
	
	JButton colorChooserButton;
	JButton sendButton;
	JButton sendFileButton;
	JButton closeButton;
	
	JEditorPane conversationPane;
	
	JTextArea typingPane;
	
	JScrollPane scrollPane;
	
	GridBagConstraints c;
	
	ChatController theChatController;
	
	Color colorChosen;
	
	JScrollBar vertical;
	
	HTMLEditorKit editor;
	
	int index;
	
	public ChatView(int indexIn){
		index = indexIn;
		
		theChatController = new ChatController(this);
		colorChosen = Color.BLACK;
		
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		conversationPane = new JEditorPane("text/html",null);
		conversationPane.setEditable(false);
		conversationPane.setText("<html><body>LOL</body></html>");
		editor = (HTMLEditorKit)conversationPane.getEditorKit();
		
		scrollPane = new JScrollPane(conversationPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		vertical = scrollPane.getVerticalScrollBar();
		scrollPane.setPreferredSize(new Dimension(500,400));
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=5;
		c.gridheight=3;
		c.fill = GridBagConstraints.NONE;
		add(scrollPane,c);
		
		colorChooserButton = new JButton("Färg...");
		colorChooserButton.addActionListener(this);
		c.gridx=0;
		c.gridy=3;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(colorChooserButton,c);
		
		sendFileButton = new JButton("Skicka fil");
		sendFileButton.addActionListener(this);
		c.gridx=1;
		c.gridy=3;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(sendFileButton,c);
		
		closeButton = new JButton("Stäng");
		closeButton.addActionListener(this);
		c.gridx=4;
		c.gridy=3;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		add(closeButton,c);
		
		typingPane = new JTextArea();
		typingPane.setPreferredSize(new Dimension(400,100));
		c.gridx=0;
		c.gridy=4;
		c.gridwidth=4;
		c.gridheight=2;
		c.fill = GridBagConstraints.BOTH;
		add(typingPane,c);
		
		sendButton = new JButton("Skicka");
		sendButton.addActionListener(this);
		c.gridx=4;
		c.gridy=4;
		c.gridwidth=1;
		c.gridheight=2;
		c.fill = GridBagConstraints.BOTH;
		add(sendButton,c);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == closeButton){
			// this is bad
			
		}else if(e.getSource() == sendButton){
			theChatController.sendMessage();
		}else if(e.getSource() == colorChooserButton){
			colorChosen = JColorChooser.showDialog(null, "Välj färj", colorChosen);
		}
	}
	
	public void raise(){
		vertical.setValue(vertical.getMaximum());
	}
	
	public void printToPane(String stringIn){
		conversationPane.setText(conversationPane.getText()+stringIn);
		raise();
	}
	
	public String getTextFieldContent(){
		String textFieldContent = typingPane.getText();
		typingPane.setText("");
		return textFieldContent;
	}
	
	public Color getColor(){
		return colorChosen;
	}
	
}

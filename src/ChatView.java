import java.awt.*;
import javax.swing.text.html.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
public class ChatView extends JPanel implements ActionListener{

    JButton colorChooserButton;
    JButton sendButton;
    JButton sendFileButton;
    JButton encryptButton;
    JButton closeButton;

    JEditorPane conversationPane;

    JTextArea typingPane;

    JScrollPane scrollPane;

    GridBagConstraints c;

    ChatController theChatController;

    Color colorChosen;

    JScrollBar vertical;

    HTMLEditorKit editor;

    JTabbedPane tabbedPane;

    public ChatView(JTabbedPane tabbedPaneIn, Socket socketIn){
        tabbedPane = tabbedPaneIn;

        theChatController = new ChatController(this,socketIn);
        colorChosen = Color.BLACK;

        setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        conversationPane = new JEditorPane("text/html",null);
        conversationPane.setEditable(false);
        //conversationPane.setText("<html><body>LOL</body></html>");
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

        encryptButton = new JButton("Kryptering");
        encryptButton.addActionListener(this);
        c.gridx=2;
        c.gridy=3;
        c.gridwidth=1;
        c.gridheight=1;
        c.fill=GridBagConstraints.HORIZONTAL;
        add(encryptButton, c);

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
        if(e.getSource() == closeButton)
        {
            theChatController.shutdownConnection();
            tabbedPane.remove(this);

        }
        else if(e.getSource() == sendButton)
        {
            theChatController.sendMessage();
            raise();
        }
        else if(e.getSource() == colorChooserButton)
        {
            colorChosen = JColorChooser.showDialog(null, "Välj färj", colorChosen);
        }
        else if (e.getSource() == encryptButton )
        {
             setNewCipherHandler();
        }
        
    }

    private void setNewCipherHandler()
    {

        JPanel uiPanel = new JPanel();
        uiPanel.setLayout( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();

        c.gridx=0;
        c.gridy= 0;
        c.gridwidth=3;
        c.gridheight=1;
        uiPanel.add(new JLabel("Välj skiffer och nyckel!"), c);
        
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        c.gridheight=1;
        uiPanel.add(new JLabel("Skiffer: "), c);

        c.gridx=1;
        c.gridy=1;
        c.gridwidth=2;
        c.gridheight=1;
        String[] cipherNames = CipherHandler.CIPHER_NAMES.clone();
        cipherNames[1] = cipherNames[CipherHandler.CIPHER_ENUM_CAESAR].concat(" (rekommenderas)");
        JComboBox cipherList = new JComboBox(cipherNames);
        cipherList.setSelectedIndex(CipherHandler.CIPHER_ENUM_CAESAR);
        uiPanel.add(cipherList,c);

        c.gridx=0;
        c.gridy = 2;
        c.gridwidth =1;
        uiPanel.add(new JLabel("Nyckel: "), c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth=2;
        JTextField keyField = new JTextField();
        keyField.setPreferredSize(new Dimension(400, 25));
        uiPanel.add(keyField, c);
        
        
        int result = JOptionPane.showConfirmDialog(null, uiPanel, "Skifferval och inställning", JOptionPane.OK_CANCEL_OPTION);

        CipherHandler ch;

        if (result == 0) { // the user has clicked OK
            try {
                ch = new CipherHandler(cipherList.getSelectedIndex(), keyField.getText());
            }
            catch(CipherKeyInputWrongException e)
            {
                e.printStackTrace();
                ch = null;
            }
            
        } else {
            ch = null;
        }

        if (ch != null){
            theChatController.cipherHandler = ch;
        }
            
    }

    public void raise(){
        vertical.setValue(vertical.getMinimum());
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

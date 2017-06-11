package yooha;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JComboBox;

import java.awt.Color;
import java.awt.Dimension; import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.ArrayList;


public class AddToChatHelper
{

    public static int whatChat(List<Chat> chats, Message message)
    {

        ArrayList<Chat> validChats = new ArrayList<Chat>();
        for( Chat c : chats )
        {
            if ( c.server )
            {
                validChats.add(c);
            }
        }

        String[] dialogString = new String[validChats.size()+1];
        for (int i = 0; i < validChats.size(); i++)
        {
            dialogString[i] = "Chat "+validChats.get(i).chatBackend.chat_id;
        }
        dialogString[validChats.size()] = "New chat";


        JPanel uiPanel = new JPanel();
        uiPanel.setLayout( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();

        c.gridx=0;
        c.gridy= 0;
        c.gridwidth=3;
        c.gridheight=1;
        uiPanel.add(new JLabel("Lägg till i chatt eller starta ny, "+ message.senderName + " säger:"), c);
        
        /*
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        c.gridheight=1;
        uiPanel.add(new JLabel("Skiffer: "), c);
        */

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth=3;
        JTextArea textField = new JTextArea();
        textField.setPreferredSize(new Dimension(400, 100));
        textField.setEnabled(false);
        textField.setLineWrap(true);
        if (message.request)
            textField.setText(message.requestText);
        else
            textField.setText(message.messageText);
        textField.setDisabledTextColor(Color.BLACK);
        uiPanel.add(textField, c);

        c.gridx=0;
        c.gridy=5;
        c.gridwidth=2;
        c.gridheight=1;
        //String[] cipherNames = CipherHandler.CIPHER_NAMES.clone();
        //cipherNames[1] = cipherNames[CipherHandler.CIPHER_ENUM_CAESAR].concat(" (rekommenderas)");
        JComboBox chatList = new JComboBox(dialogString);
        chatList.setSelectedIndex(dialogString.length-1);
        uiPanel.add(chatList,c);

        /*
        c.gridx=0;
        c.gridy = 2;
        c.gridwidth =1;
        uiPanel.add(new JLabel("Nyckel: "), c);
        */
        
        String dialogTitle = "Ny uppkoppling!";
        if (!message.request)
        {
            dialogTitle += " (Simpel klient)";
        }
        int result = JOptionPane.showConfirmDialog(null, uiPanel, dialogTitle, JOptionPane.OK_CANCEL_OPTION);

        if (result == 0) { // the user has clicked OK
            int choice = chatList.getSelectedIndex();
            int id = 0;
            if ( choice == dialogString.length-1)
            {
                id = -1;
            }
            else
            {
                id = validChats.get(choice).chatBackend.chat_id;
            }

            return id;
            
        } else {
            return -2;
        }

    }

    public static int removeUserQuery( List<ConnectionData> connectionDatas )
    {
        List<ConnectionData> datas = connectionDatas;

        String[] dialogString = new String[datas.size()];
        int i = 0;
        for ( ConnectionData cd : datas )
        {
            dialogString[i] = "ConnId "+cd.connectionId+ ": " + cd.nick;
            i++;
        }

        JPanel uiPanel = new JPanel();
        uiPanel.setLayout( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();

        c.gridx=0;
        c.gridy= 0;
        c.gridwidth=3;
        c.gridheight=1;
        uiPanel.add(new JLabel("Vilken användare vill du utesluta?"), c);
        
        /*
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        c.gridheight=1;
        uiPanel.add(new JLabel("Skiffer: "), c);
        */

        c.gridx=0;
        c.gridy=5;
        c.gridwidth=2;
        c.gridheight=1;
        //String[] cipherNames = CipherHandler.CIPHER_NAMES.clone();
        //cipherNames[1] = cipherNames[CipherHandler.CIPHER_ENUM_CAESAR].concat(" (rekommenderas)");
        JComboBox chatList = new JComboBox(dialogString);
        chatList.setSelectedIndex(dialogString.length-1);
        uiPanel.add(chatList,c);

        /*
        c.gridx=0;
        c.gridy = 2;
        c.gridwidth =1;
        uiPanel.add(new JLabel("Nyckel: "), c);
        */
        
        int result = JOptionPane.showConfirmDialog(null, uiPanel, "Ta bort uppkoppling", JOptionPane.OK_CANCEL_OPTION);

        if (result == 0) { // the user has clicked OK
            int choice = chatList.getSelectedIndex();
            int id = 0;
            id = datas.get(choice).connectionId;
            return id;
            
        } else {
            return -1;
        }

    }

    public static int whatUser( List<ConnectionData> connData )
    {
        List<ConnectionData> datas = connData;

        String[] dialogString = new String[datas.size()];
        int i = 0;
        for ( ConnectionData cd : datas )
        {
            dialogString[i] = "ConnId "+cd.connectionId+ ": " + cd.nick;
            i++;
        }

        JPanel uiPanel = new JPanel();
        uiPanel.setLayout( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();

        c.gridx=0;
        c.gridy= 0;
        c.gridwidth=3;
        c.gridheight=1;
        uiPanel.add(new JLabel("Vilken användare vill du skicka filen till?"), c);
        
        /*
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        c.gridheight=1;
        uiPanel.add(new JLabel("Skiffer: "), c);
        */

        c.gridx=0;
        c.gridy=5;
        c.gridwidth=2;
        c.gridheight=1;
        //String[] cipherNames = CipherHandler.CIPHER_NAMES.clone();
        //cipherNames[1] = cipherNames[CipherHandler.CIPHER_ENUM_CAESAR].concat(" (rekommenderas)");
        JComboBox chatList = new JComboBox(dialogString);
        chatList.setSelectedIndex(dialogString.length-1);
        uiPanel.add(chatList,c);

        /*
        c.gridx=0;
        c.gridy = 2;
        c.gridwidth =1;
        uiPanel.add(new JLabel("Nyckel: "), c);
        */
        
        int result = JOptionPane.showConfirmDialog(null, uiPanel, "Skicka fil", JOptionPane.OK_CANCEL_OPTION);

        if (result == 0) { // the user has clicked OK
            int choice = chatList.getSelectedIndex();
            int id = 0;
            id = datas.get(choice).connectionId;
            return id;
            
        } else {
            return -1;
        }


    }

    public static void main(String[] args) {
        List<Chat> chats = new ArrayList<Chat>();

    }
}

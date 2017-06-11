package yooha;

import yooha.network.*;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
class FRHandler implements Runnable
    {

        String filename;
        String nick;
        int filesize;
        Connection conn;
        ConnectionData connData;
        Chat chat;
        FRHandler ( Connection conn, ConnectionData connData, String nick, String filename, int filesize, Chat chat )
        {
            this.conn = conn;
            this.connData = connData;
            this.filename = filename;
            this.filesize = filesize;
            this.nick = nick;
            this.chat = chat;
        }

        public void run()
        {
            JPanel uiPanel = new JPanel();
            uiPanel.setLayout( new GridBagLayout() );
            GridBagConstraints c = new GridBagConstraints();

            c.gridx=0;
            c.gridy= 0;
            c.gridwidth=3;
            c.gridheight=1;
            uiPanel.add(new JLabel(nick + " vill skicka en fil till dig: "), c);

            c.gridy = 1;
            uiPanel.add(new JLabel(filename + " (" + filesize + " bytes)"), c);


            c.gridy = 2;
            uiPanel.add(new JLabel("Skriv ditt meddelande:"), c);

            c.gridy = 2;
            JTextField messageField = new JTextField();
            messageField.setPreferredSize(new Dimension(400, 25));
            uiPanel.add(messageField, c);

            int result = JOptionPane.showConfirmDialog(null, uiPanel, "Filöverföring", JOptionPane.OK_CANCEL_OPTION);


            String messageText = messageField.getText();
            if ( result == 0 )
            {
                JFileChooser chooser = new JFileChooser();
                int fcresult = chooser.showSaveDialog(chat);
                if ( fcresult == JFileChooser.APPROVE_OPTION )
                {
                    File f = chooser.getSelectedFile();
                    FileReceiver filereceiver = new FileReceiver( f, connData.inCipherHandler, filesize );
                    String xmlString = MessageDeparser.getFileResponseString(MainView.getNick(), messageText, true, filereceiver.getLocalPort());
                    conn.sendString(xmlString);
                    filereceiver.run();
                }
                else
                {
                    String xmlString = MessageDeparser.getFileResponseString(MainView.getNick(), messageText, false, 0);
                    conn.sendString(xmlString);
                }
            }
            else
            {
                String xmlString = MessageDeparser.getFileResponseString(MainView.getNick(), messageText, false, 0);
                conn.sendString(xmlString);
            }

        }

    }

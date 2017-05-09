import java.awt.*;
import javax.swing.text.html.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;



class testPanel{

    public testPanel(){

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

        System.out.println(result);


    }

    public static void main(String[] args) {
        testPanel tp = new testPanel();
    }
}


import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainView extends JFrame implements ActionListener{
	
	int listeningPort;
	
	MainController theMainController;

	JPanel thePanel;
	
	JMenuBar theMenuBar;
	
	JMenu theMenu;
	
	JMenuItem connectItem;
	JMenuItem groupChatItem;
	JMenuItem settingsItem;
	JMenuItem quitItem;
	
	static String myNick;
	static String myClan;
	
	//example model
	MainModel exampleModel = new MainModel("Bosse","haxxerz",666);
		
	public MainView(){
		super("Yooha Messenger!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(525,615));
		
		int listeningPort = Integer.parseInt(JOptionPane.showInputDialog("Välj port"));
		myNick = JOptionPane.showInputDialog("pick yo nick");
		myClan = JOptionPane.showInputDialog("plan yo clan");
		
		theMainController = new MainController(listeningPort,this);
		
		// menu & items
		theMenuBar = new JMenuBar();
		theMenu = new JMenu("Meny");
		
		connectItem = new JMenuItem("Anslut...",KeyEvent.VK_A);
		connectItem.addActionListener(this);
		groupChatItem = new JMenuItem("Ny gruppchat...",KeyEvent.VK_G);
		groupChatItem.addActionListener(this);
		settingsItem = new JMenuItem("Inställningar",KeyEvent.VK_I);
		settingsItem.addActionListener(this);
		quitItem = new JMenuItem("Avsluta",KeyEvent.VK_V);
		quitItem.addActionListener(this);
		theMenu.setMnemonic(KeyEvent.VK_M);
		
		theMenu.add(connectItem);
		theMenu.add(groupChatItem);
		theMenu.add(settingsItem);
		theMenu.add(quitItem);
		theMenuBar.add(theMenu);
		
		add(theMainController);
		setJMenuBar(theMenuBar);
		pack();
		setVisible(true);
		
		theMainController.newChat(exampleModel);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == connectItem){
			theMainController.newChat(exampleModel);
		}else if(e.getSource() == groupChatItem){
			
		}else if(e.getSource() == settingsItem){
			thePanel = new JPanel();
			thePanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx=0;
			c.gridy=0;
			c.gridwidth=1;
			thePanel.add(new JLabel("Pick yo nick"),c);
			
			JTextField nickTextField = new JTextField();
			nickTextField.setPreferredSize(new Dimension(200,25));
			c.gridx=1;
			c.gridy=0;
			c.gridwidth=3;
			thePanel.add(nickTextField,c);
			
			c.gridx=0;
			c.gridy=1;
			c.gridwidth=1;
			thePanel.add(new JLabel("Plan yo clan"),c);
			
			JTextField clanTextField = new JTextField();
			clanTextField.setPreferredSize(new Dimension(200,25));
			c.gridx=1;
			c.gridy=1;
			c.gridwidth=3;
			thePanel.add(clanTextField,c);
			
			int result = JOptionPane.showConfirmDialog(null,thePanel,"Inställningar",JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION){
				myNick = nickTextField.getText();
				myClan = clanTextField.getText();
			}
			
			
		}else if(e.getSource() == quitItem){
			System.exit(0);
		}
	}
	
	public static String getNick(){
		return myNick;
	}
	
	public static String getClan(){
		return myClan;
	}
	
}

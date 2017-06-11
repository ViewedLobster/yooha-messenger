package yooha.network;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class FileProgressGui extends JPanel implements ActionListener 
{
    public void actionPerformed( ActionEvent e )
    {
        //do nothing
    }

    JProgressBar progressBar;
    public FileProgressGui(int filesize, String filename) {
        super(new BorderLayout());

        //Create the demo's UI.

        progressBar = new JProgressBar(0, filesize);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        this.add(progressBar);

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JFrame frame = new JFrame(filename);
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);

    }

    public void setProgress(int progress)
    {
        progressBar.setValue(progress);
    }
    public void setProgressMax()
    {
        progressBar.setValue(progressBar.getMaximum());
    }

    public static void main(String[] args) {
        FileProgressGui fpg = new FileProgressGui(100, "hello");

    }

}

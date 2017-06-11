package yooha.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.io.File;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import yooha.cipher.CipherHandler;
import yooha.cipher.CipherHandlerCipher;
import yooha.cipher.CipherHandlerConstructer;
import yooha.cipher.KeyHandler;


public class FileReceiver implements Runnable
{
    Socket s;
    ServerSocket ss;
    String filepath;
    BufferedInputStream netstream;
    BufferedOutputStream filestream;
    CipherHandlerCipher chc;
    CipherHandler ch;
    File file;

    int buffer_size = 0x0000fff;
    byte[] buffer;

    FileProgressGui gui;
    int totread;
    int totreadcoarse;

    

    public FileReceiver( int port, File file, CipherHandler ch, int filesize )
    {
        try{
            ss = new ServerSocket(port);
            this.file = file;
            this.ch = ch;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        gui = new FileProgressGui( filesize, file.getName());
    }    
    
    public FileReceiver( File file, CipherHandler ch, int filesize )
    {
        try{
            ss = new ServerSocket(0);
            this.file = file;
            this.ch = ch;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        gui = new FileProgressGui( filesize, file.getName());
    }

    public int getLocalPort()
    {
        return ss.getLocalPort();
    }

    public void run()
    {
        boolean done = false;
        totread = 0;
        totreadcoarse = 0;
        try
        {

            s = ss.accept();
            netstream = new BufferedInputStream( s.getInputStream() );
            filestream = new BufferedOutputStream( new FileOutputStream( file ) );
            if (ch != null)
            {
                chc = ch.getCHCipher(netstream, false);
            }
            else
            {
                chc = null;
            }
            buffer =   new byte[buffer_size];
            while( !done )
            {
                int oldtotreadcoarse = totreadcoarse;
                int r = netstream.read(buffer, 0, buffer_size);

                if (r == -1)
                {
                    shutdown();
                    done = true;
                }
                else
                {
                    if ( this.chc != null )
                    {
                        byte[] buf = Arrays.copyOf(buffer, r);
                        buf = this.chc.decrypt(buf);
                        if ( buf != null )
                            filestream.write(buf, 0, buf.length);
                    }
                    else
                    {
                        filestream.write(buffer, 0, r);
                    }
                    totread += r;
                    totreadcoarse = totread / 0x00000fff;
                    if (oldtotreadcoarse < totreadcoarse )
                    {
                        updateGui(totread);
                    }
                }

                
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public void updateGui(int read)
    {
        gui.setProgress(read);
    }

    public void updateGuiMax()
    {
        gui.setProgressMax();
    }

    public void shutdown()
    {
        try{
            if ( chc != null )
            {
                byte[] buf = chc.doFinal();
                if ( buf != null )
                    filestream.write( buf, 0, buf.length );
            }

            netstream.close();
            filestream.close();
            s.close();
            updateGuiMax();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }

    }

    public static void main(String[] args) {
        int port = 12345;
        String filepath = "/home/magnus/Documents/tmp.txt.transferred";
        File f = new File(filepath);

        KeyHandler kh = new KeyHandler();
        CipherHandler ch = CipherHandlerConstructer.getCipherHandler("AES", kh.getDefaultKeyString("AES"), true);

        FileReceiver fr = new FileReceiver( port, f, ch, 1000);

        (new Thread(fr)).start();

    }

}

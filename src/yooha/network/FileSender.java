package yooha.network;

import java.net.Socket;
import java.net.InetAddress;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.io.File;

import yooha.cipher.CipherHandler;
import yooha.cipher.CipherHandlerCipher;
import yooha.cipher.CipherHandlerConstructer;
import yooha.cipher.KeyHandler;


public class FileSender implements Runnable
{
    Socket s;
    BufferedInputStream filestream;
    BufferedOutputStream netstream;
    CipherHandlerCipher chc;

    int buffer_size = 0x0000fff;
    byte[] buffer;
    int totwritten;
    int totwrittencoarse;

    private FileProgressGui gui;

    public FileSender( String host, int port, File f, CipherHandler ch )
    {
        try
        {
            s = new Socket(host, port);
            filestream = new BufferedInputStream( new FileInputStream( f ) );
            netstream = new BufferedOutputStream( s.getOutputStream() );
            if (ch != null)
            {
                chc = ch.getCHCipher();
            }
            else
            {
                chc = null;
            }
            buffer =   new byte[buffer_size];
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }


        gui = new FileProgressGui((int)f.length(), f.getName());

    }
    
    public FileSender( InetAddress host, int port, File f, CipherHandler ch ) {
        try
        {
            s = new Socket(host, port);
            filestream = new BufferedInputStream( new FileInputStream( f ) );
            netstream = new BufferedOutputStream( s.getOutputStream() );
            if (ch != null)
            {
                chc = ch.getCHCipher();
            }
            else
            {
                chc = null;
            }
            buffer =   new byte[buffer_size];
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }


        gui = new FileProgressGui((int)f.length(), f.getName());

    }
    public void run()
    {
        boolean done = false;
        totwritten = 0;
        totwrittencoarse = 0;
        try
        {
            while( !done )
            {
                int r = filestream.read(buffer, 0, buffer_size);
                int oldtotwrittencoarse = totwrittencoarse;
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
                        buf = this.chc.encrypt(buf);
                        if ( buf != null )
                            netstream.write(buf, 0, buf.length);
                    }
                    else
                    {
                        netstream.write(buffer, 0, r);
                    }
                    totwritten += r;
                    totwrittencoarse = totwritten / 0x0000fff;
                    if (totwrittencoarse > oldtotwrittencoarse)
                        updateGui(totwritten);
                }

            }

        }
        catch ( IOException e )
        {e.printStackTrace();}
    }

    public void shutdown()
    {
        try{
            if ( chc != null )
            {
                byte[] buf = chc.doFinal();
                if ( buf != null )
                    netstream.write( buf, 0, buf.length );
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

    public void updateGui(int written)
    {
        gui.setProgress(written);
    }

    public void updateGuiMax()
    {
        gui.setProgressMax();
    }

    public static void main(String[] args) {
        String filepath = "/home/magnus/tmp.txt";

        KeyHandler kh = new KeyHandler();
        CipherHandler ch = CipherHandlerConstructer.getCipherHandler("AES", kh.getDefaultKeyString("AES"), false);

        FileSender fs = new FileSender( "127.0.0.1", 12345, new File(filepath), ch);
        (new Thread(fs)).start();
    }

}

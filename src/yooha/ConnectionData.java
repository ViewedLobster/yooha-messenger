package yooha;

import yooha.cipher.CipherHandler;

import java.util.Date;
import java.io.File;

public class ConnectionData
{

    public String nick;
    public String clan;
    public final int connectionId;
    public long keyRequestTimeMillis = -1;
    public String keyRequestType;
    public String keyRequestKey;
    public CipherHandler outCipherHandler;
    public CipherHandler inCipherHandler;
    public long filesendtimestamp;
    public File fileToSend;
    

    public ConnectionData ( String nick, String clan, int connectionId )
    {
        this.nick = nick;
        this.clan = clan;
        this.connectionId = connectionId;
        this.inCipherHandler = null;
        this.outCipherHandler = null;
    }

    public void setSentKeyRequest( String type, String key )
    {
        this.keyRequestType = type;
        Date d = new Date();
        this.keyRequestTimeMillis = d.getTime();
        this.keyRequestKey = key;
    }

    public boolean hasSentKeyRequest ( String type )
    {
        Date d = new Date();
        if ( type.equals( keyRequestType ) && d.getTime() - 60000 <= keyRequestTimeMillis && keyRequestTimeMillis >= 0 )
        {
            return true;
        }
        else return false;
    }
    public boolean hasSentFileRequest ()
    {
        Date d = new Date();
        if ( fileToSend != null && d.getTime() - 60000 <= keyRequestTimeMillis && keyRequestTimeMillis >= 0 )
        {
            return true;
        }
        else return false;
    }

    public void resetSentKeyRequest ()
    {
        keyRequestTimeMillis = -1;
        keyRequestType = null;
    }

    public void setInCipherHandler(CipherHandler ch)
    {
        this.inCipherHandler = ch;
    }

    public void setOutCipherHandler(CipherHandler ch)
    {
        this.outCipherHandler = ch;
    }

}

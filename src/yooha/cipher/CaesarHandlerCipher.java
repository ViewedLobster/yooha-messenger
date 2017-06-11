package yooha.cipher;

public class CaesarHandlerCipher extends CipherHandlerCipher
{
    int key;
    public CaesarHandlerCipher(CaesarHandler ch)
    {
        this.key = ch.key;
    }


    public byte[] encrypt( byte[] buffer )
    {
        byte[] out = new byte[buffer.length];

        for (int i = 0; i < buffer.length; i++)
        {
            out[i] = (byte)( (buffer[i] + key) & 0x00ff); 
        }

        return out;
    }
    
    public byte[] decrypt( byte[] buffer )
    {
        byte[] out = new byte[buffer.length];

        for (int i = 0; i < buffer.length; i++)
        {
            out[i] = (byte)( (buffer[i] - key) & 0x00ff); 
        }

        return out;
    }

    public byte[] doFinal()
    {
        return null;
    }
}

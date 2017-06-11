package yooha.cipher;

import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

public abstract class CipherHandler
{

    public final String type;
    public final int blockLength;

    public CipherHandler( String type, int blockLength )
    {
        this.type = type;
        this.blockLength = blockLength;
    }

    public abstract String encryptText( String s );
    public abstract String decryptText( String s );
    public abstract CipherHandlerCipher getCHCipher();
    public abstract CipherHandlerCipher getCHCipher(InputStream in, boolean encryption);

    public static final String[] cipherStrings = { "caesar", "AES", "TT"};

    public static boolean supportedCipher( String type )
    {
        for ( String s : cipherStrings )
        {
            if ( s.equals( type ) )
            {
                return true;
            }
        }
        return false;
    }

}

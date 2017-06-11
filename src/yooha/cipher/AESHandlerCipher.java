package yooha.cipher;

import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import java.nio.charset.StandardCharsets;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;

import java.io.InputStream;
import java.io.IOException;

public class AESHandlerCipher extends CipherHandlerCipher
{

    Cipher cipher;
    boolean encryption;
    boolean gotIV = true;

    public AESHandlerCipher (InputStream in, AESHandler aesHandler ) throws IOException
    {
        this.encryption = false;

        byte[] ivBuffer = new byte[16];

        int read = 0;
        int one_read = -1;
        while( read < 16 )
        {
            one_read = in.read(ivBuffer, read, 16-read);
            if (one_read == -1 )
                break;
            read += one_read;
        }

        if (one_read == -1)
            cipher = null;
        else
        {
            cipher = aesHandler.getDecryptionAESCipher(ivBuffer);
        }
    }

    public AESHandlerCipher ( AESHandler aesHandler )
    {
        this.encryption = true;
        this.cipher = aesHandler.getEncryptionAESCipher();
    }

    public byte[] decrypt( byte[] buffer )
    {
        if (!this.encryption)
            return cipher.update(buffer);
        else
            return null;
    }

    public byte[] encrypt( byte[] buffer )
    {
        if (this.encryption)
        {
            if (gotIV)
            {
                byte[] b = cipher.update(buffer);
                gotIV = false;
                if (b == null)
                    return cipher.getIV();
                else 
                {
                    // concatenate iv and encryption
                    byte[] iv = cipher.getIV();
                    byte[] buf = new byte[iv.length + b.length];
                    int i = 0;
                    for ( int j = 0; j < iv.length; j++ )
                    {
                        buf[i++] = iv[j];
                    }
                    for ( int j = 0; j < b.length; j++ )
                    {
                        buf[i++] = b[j];
                    }
                    return buf;
                }
            }
            else
                return cipher.update(buffer);
        }
        else
            return null;
    }

    public byte[] doFinal()
    {
        try
        {
            return cipher.doFinal();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}

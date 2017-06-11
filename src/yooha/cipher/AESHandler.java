package yooha.cipher;

import yooha.Utls;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.IOException;

public class AESHandler extends CipherHandler
{

    public final String keyString;
    private final Key key;
    private Cipher cipher;


    AESHandler ( String keyString ) throws KeyFormatWrongException
    {
        super("AES", 16);

        if ( !Utls.isHexString(keyString) || keyString.length() != 32 )
            throw new KeyFormatWrongException( "The key string format is wrong: "+keyString);
        this.keyString =  keyString;

        byte[] keyBytes = Utls.hexStringToByteArray(keyString);
        this.key = new SecretKeySpec(keyBytes, 0, 16, "AES");
    }

    /* Takes a plaintext string and outputs an encrypted hex string */
    public String encryptText( String plaintext )
    {
        byte[] bytePlaintext = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] byteCiphertext;
        IvParameterSpec iv;

        try{
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            iv = new IvParameterSpec(cipher.getIV());
            byteCiphertext = cipher.doFinal(bytePlaintext);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            iv = null;
            byteCiphertext = null;
        }

        if (byteCiphertext != null) {
            return (new String(Utls.byteArrayToHexCharArray(iv.getIV()))) +
                    (new String(Utls.byteArrayToHexCharArray(byteCiphertext)));
        } else {
            return null;
        }

    }

    /* Takes a hex string (encrypted) and outputs a plaintext string */
    public String decryptText( String ciphertext )
    {
        IvParameterSpec iv = new IvParameterSpec(Utls.hexStringToByteArray(ciphertext.substring(0, 32)));
        byte[] bytePlaintext;
        byte[] byteCiphertext = Utls.hexStringToByteArray(ciphertext.substring(32));

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            bytePlaintext = cipher.doFinal(byteCiphertext);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            bytePlaintext = null;
        }

        if (bytePlaintext != null) {
            return new String(bytePlaintext);
        }
        else
        {
            return null;
        }
    }

    public Cipher getDecryptionAESCipher( byte[] ivBytes )
    {
        try{
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, key, iv);
            return c;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public Cipher getEncryptionAESCipher()
    {
        try{
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, key);
            return c;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public CipherHandlerCipher getCHCipher( InputStream in, boolean encryption )
    {
        if ( encryption )
        {
            return new AESHandlerCipher( this );
        }
        else
        {
            try{
                return new AESHandlerCipher( in, this );
            } catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    public CipherHandlerCipher getCHCipher ()
    {
        return new AESHandlerCipher( this );
    }

}

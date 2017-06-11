package yooha.cipher;

import yooha.Utls;
import java.io.InputStream;

public class CaesarHandler extends CipherHandler
{

    public final String keyString;
    final int key;

    CaesarHandler ( String keyString ) throws KeyFormatWrongException
    {
        super("caesar", 1);
        if ( !Utls.isHexString(keyString) || keyString.length() != 2 )
            throw new KeyFormatWrongException( "Key string format is wrong: "+keyString);
        this.keyString = keyString;
        this.key = Integer.parseInt(keyString, 16) & 0x000000ff;
    }

    public String toString()
    {
        return "CaesarHandler: " + keyString;
    }

    public String encryptText( String plaintext )
    {
        int tmpChar;
        byte[] intermediaryByteString = new byte[plaintext.length()];

        // Go through each of the characters and add the key, put the result
        // in a byte array
        for (int i = 0; i < plaintext.length(); i++) {
            tmpChar = (plaintext.charAt(i) + key) & 0x00ff ;
            intermediaryByteString[i] = (byte)tmpChar;
        }

        // COnvert the result to a hex string and return 
        return new String(Utls.byteArrayToHexCharArray(intermediaryByteString));
    }

    public String decryptText( String ciphertext )
    {
        byte[] byteArray = Utls.hexStringToByteArray(ciphertext);

        StringBuilder outputSB = new StringBuilder();
        int tmpChar;
        char tmpCharacter;

        for (int i = 0; i < byteArray.length; i++) {
            tmpChar = ((char)byteArray[i]) & 0x00ff;
            tmpChar = (tmpChar - key) & 0x00ff;
            tmpCharacter  = (char)tmpChar;
            outputSB.append(tmpCharacter);
        }

        return outputSB.toString();
    }

    public CipherHandlerCipher getCHCipher(InputStream in, boolean encryption)
    {
        return new CaesarHandlerCipher(this);
    }

    public CipherHandlerCipher getCHCipher()
    {
        return new CaesarHandlerCipher(this);
    }

    public static void main(String[] args) {
        String key = "AB";

        try
        {
        CaesarHandler ch = new CaesarHandler(key);
        String ciphertext = "E71F10231FCB0E1A171A1DE8CDCEDBDBDBDBDBDBCDE91310151E0C19E7DA1F10231FE9";

        System.out.println(ch.decryptText(ciphertext));
        }
        catch(Exception e)
        {}


    }


}

package yooha.cipher;


public class KeyHandler
{


    public String defaultAESKey;
    public String defaultCaesarKey;
    private String defaultType;

    public KeyHandler()
    {
        defaultAESKey = "0123456789ABCDEF0123456789ABCDEF";
        defaultCaesarKey = "AB";
        defaultType = "AES";
    }

    public String getDefaultKeyString( String type )
    {
        if ( "caesar".equals(type) )
            return defaultCaesarKey;
        if ( "AES".equals(type) )
            return defaultAESKey;

        return null;
    }

    public String getDefaultTypeString()
    {
        return defaultType;
    }

    public void setDefaultTypeString( String s )
    {
        if (CipherHandler.supportedCipher( s ))
            defaultType = s;
    }
}

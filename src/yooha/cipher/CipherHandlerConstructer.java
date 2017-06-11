package yooha.cipher;



public class CipherHandlerConstructer
{


    public static CipherHandler getCipherHandler( String type, String key, boolean incoming )
    {

        try {
            if ( "AES".equals(type) )
            {
                return new AESHandler(key);
            }
            else if ( "caesar".equals(type) )
            {
                return new CaesarHandler(key);
            }
            else return null;
        }
        catch( KeyFormatWrongException e )
        {
            e.printStackTrace();
        }

        return null;

    }

}

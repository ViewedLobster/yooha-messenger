package yooha.cipher;

public class TruthTellerHandler extends CipherHandler
{


    public TruthTellerHandler()
    {
        super("tt", 1);
    }

    public String encryptText(String s)
    {
        //TODO hex encode
        return s;
    }

    public String decryptText( String s)
    {
        return s;
    }
}

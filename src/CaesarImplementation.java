import java.lang.StringBuilder;



class CaesarImplementation implements CipherImplementation {
    
    private int key;

    public CaesarImplementation (String keyString) throws CipherKeyInputWrongException {
        key = Integer.parseInt(keyString);
        
        if (key > 255 || key < 1) {
            throw new CipherKeyInputWrongException("The key must be a number between 1 an 255 inclusive");
        }

    }

    
    public String encryptToHexString(String inputString)
    {
        StringBuilder outputSB = new StringBuilder();
        int tmpChar;
        byte[] intermediaryByteString = new byte[inputString.length()];

        // Go through each of the characters and add the key, put the result
        // in a byte array
        for (int i = 0; i < inputString.length(); i++) {
            tmpChar = (inputString.charAt(i) + key) & 0x00ff ;
            intermediaryByteString[i] = (byte)tmpChar;
        }

        // COnvert the result to a hex string and return 
        return new String(Utls.byteArrayToHexCharArray(intermediaryByteString));
    }

    public String decryptFromHexString( String hexString )
    {
        byte[] byteArray = Utls.hexStringToByteArray(hexString);

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

}

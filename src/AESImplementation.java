import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.charset.StandardCharsets;



class AESImplementation implements CipherImplementation {

    Key key;
    Cipher cipher;


    public AESImplementation (String keyHexString, int numBits) throws CipherKeyInputWrongException {
        byte[] keyByteArray = Utls.hexStringToByteArray(keyHexString);

        int numBytes = numBits/8;
        if (numBytes != keyHexString.length()/2) {
            throw new CipherKeyInputWrongException("The key for AES must be a 32 characters long hexadecimal string");
        }
        key = new SecretKeySpec(keyByteArray, 0, numBytes, "AES");

    }

    public String encryptToHexString(String plaintext)
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

    public String decryptFromHexString(String ciphertext)
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


}

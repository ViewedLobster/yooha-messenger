

/*
 * Just a stub class for a cipher handler
 *
 */


class CipherHandler {

    public String cipherType;
    public String key;
    public int cipherNumber;

    private CipherImplementation cipherImp = null;

    public static final int CIPHER_ENUM_AES = 0;
    public static final int CIPHER_ENUM_CAESAR = 1;
    public static final int CIPHER_ENUM_SATAN = 2;
    public static final String[] CIPHER_NAMES = {"AES", "Caesar shift", "Devil's shuffle"};
    public static final String[] CIPHER_STRINGs = {"aes", "caesar", "devil"};

    public CipherHandler (String ctIn, String keyIn)
    {
        cipherType = ctIn;
        key = keyIn;
    }

    public CipherHandler (int cipherEnum, String keyIn) throws CipherKeyInputWrongException 
    {
        key = keyIn;
        cipherType = CIPHER_NAMES[cipherEnum];
        cipherNumber = cipherEnum;
        switch (cipherEnum) {
            case CIPHER_ENUM_AES:
                cipherImp = new AESImplementation(keyIn, 128);
                break;
            case CIPHER_ENUM_CAESAR:
                cipherImp = new CaesarImplementation(keyIn);
                break;
            case CIPHER_ENUM_SATAN:
                cipherImp = null;
        }
    }

    public String encrypt(String plaintext){
        if (cipherImp != null)
        {
            return cipherImp.encryptToHexString(plaintext);
        }
        else
        {
            return null;
        }
    }

    public String decrypt(String ciphertextHex) {
        if (cipherImp != null) {
            return cipherImp.decryptFromHexString(ciphertextHex);
        }
        else
        {
            return null;
        }
    }

}

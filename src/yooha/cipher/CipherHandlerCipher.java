package yooha.cipher;


public abstract class CipherHandlerCipher
{

    public abstract byte[] decrypt(byte[] buffer );
    public abstract byte[] encrypt( byte[] buffer );
    public abstract byte[] doFinal();
}

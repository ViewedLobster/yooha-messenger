

class testAES
{
    public static void main(String[] args) throws Exception {
        String keystring = "1234567890ABCDEF1234567890ABCDEF";
        AESImplementation aes = new AESImplementation(keystring, 128);

        String testString = "Hejsan det e satan";

        String enc = aes.encryptToHexString(testString);
        System.out.println(enc);

        String dec = aes.decryptFromHexString(enc);
        System.out.println(dec);
    }
}

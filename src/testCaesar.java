

class testCaesar
{

    public static void main(String[] args) throws Exception {
        CaesarImplementation caesar = new CaesarImplementation("1");

        String testString = "abcdefghijklmnopqrstuvxyz";

        String enc = caesar.encryptToHexString(testString);
        System.out.println(enc);

        String dec = caesar.decryptFromHexString(enc);
        System.out.println(dec);
    }
}

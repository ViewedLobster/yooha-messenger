


class testUtils {



    public static void main(String[] args) {
        String testString = "abcdefghijklmnopqrstuvxyz";

        char[] charray = testString.toCharArray();

        byte[] b = new byte[charray.length];

        for (int i = 0; i < b.length; i++) {
            b[i] = (byte)charray[i];
        }

        charray = Utls.byteArrayToHexCharArray(b);
        System.out.println(new String(charray));


        byte[] a = Utls.hexStringToByteArray(new String(charray));

        char[] charray2 = new char[a.length];

        for (int i = 0; i < charray2.length; i++) {
            charray2[i] = (char)(((int)a[i]) & 0x00ff);
        }

        System.out.println(new String(charray2));


    }
}

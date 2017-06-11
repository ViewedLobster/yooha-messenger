package yooha;

public class Utls {

    private static final char[] hexLookup = "0123456789ABCDEF".toCharArray();
    private static final boolean[] isHexChar = initIsHexChar();

    private static boolean[] initIsHexChar()
    {
        boolean[] bArray = new boolean[0x00ff];
        for (char c : hexLookup)
        {
            bArray[c] = true;
        }
        return bArray;
    }

    public static boolean isHexString( String s )
    {
        for (char c : s.toCharArray() )
        {
            if ( 0 > c || c > 0x00ff)
                return false;
            if (! isHexChar[c])
                return false;
        }
        return true;
    }

    // Should be quite fast
    public static char[] byteArrayToHexCharArray( byte[] byteArray )
    {
        char[] output = new char[byteArray.length*2];
        char bitTranslation;
        int tmpInt;
        int i = 0;
    
        while ( i < byteArray.length*2 ) {
            tmpInt = ((int)byteArray[i >> 1]) & 0xff; // >> 1 equivalent to / 2
            bitTranslation = hexLookup[tmpInt >> 4];
            output[i++] = bitTranslation;
            bitTranslation = hexLookup[tmpInt & 0x0f];
            output[i++] = bitTranslation;
        }

        return output;

    }

    public static String byteArrayToHexString ( byte[] byteArray )
    {
        return new String(byteArrayToHexCharArray( byteArray ));
    }

    // Kind of simple method
    public static byte[] hexCharArrayToByteArray( char[] charArray ){
        byte[] output = new byte[charArray.length / 2];
        String charString = new String(charArray);
        int i = 0;

        while( i < charArray.length ){
            output[i>>1] = Integer.valueOf(charString.substring(i, i+2), 16).byteValue();
            i += 2;
        }

        return output;
    }

    public static byte[] hexStringToByteArray ( String hexString ) {
        byte[] output = new byte[hexString.length() / 2];
        int i = 0;

        while( i < hexString.length() ){
            output[i >> 1] = Integer.valueOf(hexString.substring(i, i+2), 16).byteValue(); // >> 1 equivalent to dividing by 2
            i += 2;
        }

        return output;
    }

    public static void main(String[] args) {
        String a = "0123456789ABCDEF0123456789ABCDEF";

        System.out.println(isHexString(a));
    }


}

import java.lang.StringBuilder;

class Utls {

    private static final char[] hexLookup = "0123456789ABCDEF".toCharArray();

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


}

// Public domain Netstrings encoder/decoder class for Java
// Version 1.0 - Written by Brian Nez <http://NEZzen.net/>

public class Netstrings {

    public static String encode(String message) {
        int length = message.length();
        message = new String(length + ":" + message + ",");
        return message;
    }

    public static String decode(String original) {
        String result = original.split(":", 2)[1];
        if(result.length()-1 != Integer.parseInt(original.split(":")[0])) {
            System.out.println("Netstring integrity error: Expected length " + original.split(":")[0] + " but got length " + (result.length()-1) );
            return null;
        } else {
        return result.substring(0, result.length()-1);
        }
    }

}

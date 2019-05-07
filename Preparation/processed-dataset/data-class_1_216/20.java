/**
     * Encode a string using Base64 encoding. Used when storing passwords
     * as cookies.
     *
     * This is weak encoding in that anyone can use the decodeString
     * routine to reverse the encoding.
     *
     * @param str
     * @return String
     * @throws IOException
     */
public static String encodeString(String str) throws IOException {
    String encodedStr = new String(Base64.encodeBase64(str.getBytes()));
    return (encodedStr.trim());
}

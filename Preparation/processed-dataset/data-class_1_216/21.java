/**
     * Decode a string using Base64 encoding.
     *
     * @param str
     * @return String
     * @throws IOException
     */
public static String decodeString(String str) throws IOException {
    String value = new String(Base64.decodeBase64(str.getBytes()));
    return (value);
}

/**
     * Convert a byte array into a Base64 string (as used in mime formats)
     */
public static String toBase64(byte[] aValue) {
    final String m_strBase64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    int byte1;
    int byte2;
    int byte3;
    int iByteLen = aValue.length;
    StringBuffer tt = new StringBuffer();
    for (int i = 0; i < iByteLen; i += 3) {
        boolean bByte2 = (i + 1) < iByteLen;
        boolean bByte3 = (i + 2) < iByteLen;
        byte1 = aValue[i] & 0xFF;
        byte2 = (bByte2) ? (aValue[i + 1] & 0xFF) : 0;
        byte3 = (bByte3) ? (aValue[i + 2] & 0xFF) : 0;
        tt.append(m_strBase64Chars.charAt(byte1 / 4));
        tt.append(m_strBase64Chars.charAt((byte2 / 16) + ((byte1 & 0x3) * 16)));
        tt.append(((bByte2) ? m_strBase64Chars.charAt((byte3 / 64) + ((byte2 & 0xF) * 4)) : '='));
        tt.append(((bByte3) ? m_strBase64Chars.charAt(byte3 & 0x3F) : '='));
    }
    return tt.toString();
}

/** Decodes a stream that has the LZWDecode filter.
     * @param in the input data
     * @return the decoded data
     */
public static byte[] LZWDecode(byte in[]) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    LZWDecoder lzw = new LZWDecoder();
    lzw.decode(in, out);
    return out.toByteArray();
}

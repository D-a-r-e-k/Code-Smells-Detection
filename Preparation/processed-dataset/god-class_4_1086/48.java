/** Decodes a stream that has the FlateDecode filter.
     * @param in the input data
     * @return the decoded data
     */
public static byte[] FlateDecode(byte in[]) {
    byte b[] = FlateDecode(in, true);
    if (b == null)
        return FlateDecode(in, false);
    return b;
}

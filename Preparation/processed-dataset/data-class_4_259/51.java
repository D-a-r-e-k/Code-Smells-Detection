/** Decodes a stream that has the ASCIIHexDecode filter.
     * @param in the input data
     * @return the decoded data
     */
public static byte[] ASCIIHexDecode(byte in[]) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    boolean first = true;
    int n1 = 0;
    for (int k = 0; k < in.length; ++k) {
        int ch = in[k] & 0xff;
        if (ch == '>')
            break;
        if (PRTokeniser.isWhitespace(ch))
            continue;
        int n = PRTokeniser.getHex(ch);
        if (n == -1)
            throw new RuntimeException(MessageLocalization.getComposedMessage("illegal.character.in.asciihexdecode"));
        if (first)
            n1 = n;
        else
            out.write((byte) ((n1 << 4) + n));
        first = !first;
    }
    if (!first)
        out.write((byte) (n1 << 4));
    return out.toByteArray();
}

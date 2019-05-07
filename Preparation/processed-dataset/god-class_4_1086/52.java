/** Decodes a stream that has the ASCII85Decode filter.
     * @param in the input data
     * @return the decoded data
     */
public static byte[] ASCII85Decode(byte in[]) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int state = 0;
    int chn[] = new int[5];
    for (int k = 0; k < in.length; ++k) {
        int ch = in[k] & 0xff;
        if (ch == '~')
            break;
        if (PRTokeniser.isWhitespace(ch))
            continue;
        if (ch == 'z' && state == 0) {
            out.write(0);
            out.write(0);
            out.write(0);
            out.write(0);
            continue;
        }
        if (ch < '!' || ch > 'u')
            throw new RuntimeException(MessageLocalization.getComposedMessage("illegal.character.in.ascii85decode"));
        chn[state] = ch - '!';
        ++state;
        if (state == 5) {
            state = 0;
            int r = 0;
            for (int j = 0; j < 5; ++j) r = r * 85 + chn[j];
            out.write((byte) (r >> 24));
            out.write((byte) (r >> 16));
            out.write((byte) (r >> 8));
            out.write((byte) r);
        }
    }
    int r = 0;
    // We'll ignore the next two lines for the sake of perpetuating broken PDFs 
    //        if (state == 1) 
    //            throw new RuntimeException(MessageLocalization.getComposedMessage("illegal.length.in.ascii85decode")); 
    if (state == 2) {
        r = chn[0] * 85 * 85 * 85 * 85 + chn[1] * 85 * 85 * 85 + 85 * 85 * 85 + 85 * 85 + 85;
        out.write((byte) (r >> 24));
    } else if (state == 3) {
        r = chn[0] * 85 * 85 * 85 * 85 + chn[1] * 85 * 85 * 85 + chn[2] * 85 * 85 + 85 * 85 + 85;
        out.write((byte) (r >> 24));
        out.write((byte) (r >> 16));
    } else if (state == 4) {
        r = chn[0] * 85 * 85 * 85 * 85 + chn[1] * 85 * 85 * 85 + chn[2] * 85 * 85 + chn[3] * 85 + 85;
        out.write((byte) (r >> 24));
        out.write((byte) (r >> 16));
        out.write((byte) (r >> 8));
    }
    return out.toByteArray();
}

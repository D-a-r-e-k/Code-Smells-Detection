/** Gets the code pages supported by the font.
     * @return the code pages supported by the font
     */
@Override
public String[] getCodePagesSupported() {
    long cp = ((long) os_2.ulCodePageRange2 << 32) + (os_2.ulCodePageRange1 & 0xffffffffL);
    int count = 0;
    long bit = 1;
    for (int k = 0; k < 64; ++k) {
        if ((cp & bit) != 0 && codePages[k] != null)
            ++count;
        bit <<= 1;
    }
    String ret[] = new String[count];
    count = 0;
    bit = 1;
    for (int k = 0; k < 64; ++k) {
        if ((cp & bit) != 0 && codePages[k] != null)
            ret[count++] = codePages[k];
        bit <<= 1;
    }
    return ret;
}

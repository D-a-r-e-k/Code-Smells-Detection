/**
     * Returns a four character code representing the sound of the given
     * <code>String</code>. Non-ASCCI characters in the
     * input <code>String</code> are ignored. <p>
     *
     * This method was rewritten for HSQLDB to comply with the description at
     * <a href="http://www.archives.gov/genealogy/census/soundex.html">
     * http://www.archives.gov/genealogy/census/soundex.html </a>.<p>
     * @param s the <code>String</code> for which to calculate the 4 character
     *      <code>SOUNDEX</code> value
     * @return the 4 character <code>SOUNDEX</code> value for the given
     *      <code>String</code>
     */
public static char[] soundex(String s) {
    if (s == null) {
        return null;
    }
    s = s.toUpperCase(Locale.ENGLISH);
    int len = s.length();
    char[] b = new char[] { '0', '0', '0', '0' };
    char lastdigit = '0';
    for (int i = 0, j = 0; i < len && j < 4; i++) {
        char c = s.charAt(i);
        char newdigit;
        if ("AEIOUY".indexOf(c) != -1) {
            newdigit = '7';
        } else if (c == 'H' || c == 'W') {
            newdigit = '8';
        } else if ("BFPV".indexOf(c) != -1) {
            newdigit = '1';
        } else if ("CGJKQSXZ".indexOf(c) != -1) {
            newdigit = '2';
        } else if (c == 'D' || c == 'T') {
            newdigit = '3';
        } else if (c == 'L') {
            newdigit = '4';
        } else if (c == 'M' || c == 'N') {
            newdigit = '5';
        } else if (c == 'R') {
            newdigit = '6';
        } else {
            continue;
        }
        if (j == 0) {
            b[j++] = c;
            lastdigit = newdigit;
        } else if (newdigit <= '6') {
            if (newdigit != lastdigit) {
                b[j++] = newdigit;
                lastdigit = newdigit;
            }
        } else if (newdigit == '7') {
            lastdigit = newdigit;
        }
    }
    return b;
}

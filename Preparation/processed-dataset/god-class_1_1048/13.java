//-------------------------------------------------------------------------- 
/** Convert string to string array. */
public static String[] stringToStringArray(String instr, String delim) throws NoSuchElementException, NumberFormatException {
    StringTokenizer toker = new StringTokenizer(instr, delim);
    String stringArray[] = new String[toker.countTokens()];
    int i = 0;
    while (toker.hasMoreTokens()) {
        stringArray[i++] = toker.nextToken();
    }
    return stringArray;
}

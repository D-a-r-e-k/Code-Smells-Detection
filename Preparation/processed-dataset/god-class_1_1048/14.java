//-------------------------------------------------------------------------- 
/** Convert string to integer array. */
public static int[] stringToIntArray(String instr, String delim) throws NoSuchElementException, NumberFormatException {
    StringTokenizer toker = new StringTokenizer(instr, delim);
    int intArray[] = new int[toker.countTokens()];
    int i = 0;
    while (toker.hasMoreTokens()) {
        String sInt = toker.nextToken();
        int nInt = Integer.parseInt(sInt);
        intArray[i++] = new Integer(nInt).intValue();
    }
    return intArray;
}

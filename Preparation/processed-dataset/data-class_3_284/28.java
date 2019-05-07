/** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
private static char[] zzUnpackCMap(String packed) {
    char[] map = new char[0x10000];
    int i = 0;
    /* index in packed string  */
    int j = 0;
    /* index in unpacked array */
    while (i < 132) {
        int count = packed.charAt(i++);
        char value = packed.charAt(i++);
        do map[j++] = value; while (--count > 0);
    }
    return map;
}

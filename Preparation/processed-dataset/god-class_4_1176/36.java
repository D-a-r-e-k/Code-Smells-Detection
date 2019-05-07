/**
     * This method checks if a given character is between C0 or C1 range
     * of Control characters.
     * This method is added to support Control Characters for XML 1.1
     * If a given character is TAB (0x09), LF (0x0A) or CR (0x0D), this method
     * return false. Since they are whitespace characters, no special processing is needed.
     * 
     * @param ch
     * @return boolean
     */
private static boolean isCharacterInC0orC1Range(char ch) {
    if (ch == 0x09 || ch == 0x0A || ch == 0x0D)
        return false;
    else
        return (ch >= 0x7F && ch <= 0x9F) || (ch >= 0x01 && ch <= 0x1F);
}

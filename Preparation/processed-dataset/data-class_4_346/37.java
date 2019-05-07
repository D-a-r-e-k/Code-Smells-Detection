/**
     * This method checks if a given character either NEL (0x85) or LSEP (0x2028)
     * These are new end of line charcters added in XML 1.1.  These characters must be
     * written as Numeric Character References (NCR) in XML 1.1 output document.
     * 
     * @param ch
     * @return boolean
     */
private static boolean isNELorLSEPCharacter(char ch) {
    return (ch == 0x85 || ch == 0x2028);
}

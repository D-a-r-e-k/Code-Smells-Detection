// isValidNameStartChar(int):  boolean  
// returns true if the given character is  
// a valid NCName character with respect to the version of  
// XML understood by this scanner.  
protected boolean isValidNCName(int value) {
    return (XMLChar.isNCName(value));
}

// isValidNameChar(int):  boolean  
// returns true if the given character is   
// a valid nameStartChar with respect to the version of  
// XML understood by this scanner.  
protected boolean isValidNameStartChar(int value) {
    return (XMLChar.isNameStart(value));
}

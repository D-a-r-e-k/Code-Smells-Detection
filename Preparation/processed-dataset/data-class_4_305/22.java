// isInvalidLiteral(int):  boolean  
// returns true if the given character is   
// a valid nameChar with respect to the version of  
// XML understood by this scanner.  
protected boolean isValidNameChar(int value) {
    return (XMLChar.isName(value));
}

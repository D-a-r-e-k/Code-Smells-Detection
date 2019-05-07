// isInvalid(int):  boolean  
// returns true if the given character is not  
// valid or may not be used outside a character reference   
// with respect to the version of XML understood by this scanner.  
protected boolean isInvalidLiteral(int value) {
    return (XMLChar.isInvalid(value));
}

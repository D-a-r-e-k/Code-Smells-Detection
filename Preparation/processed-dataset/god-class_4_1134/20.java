// returns true if the given character is not  
// valid with respect to the version of  
// XML understood by this scanner.  
protected boolean isInvalid(int value) {
    return (XMLChar.isInvalid(value));
}

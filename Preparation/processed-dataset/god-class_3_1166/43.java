/** Finds the next identifier (following a non-whitespace character) in the document starting at start. Assumes that
    * read lock and _reduced lock are already held. */
private String getNextIdentifier(final int startPos) throws ClassNameNotFoundException {
    //    _log.log("getNextIdentifer(" + startPos + ") called"); 
    //    int index = 0; 
    //    int length = 0; 
    //    int endIndex = 0; 
    //    String text = ""; 
    //    int i; 
    try {
        // first find index of first non whitespace (from the index in document) 
        int index = getFirstNonWSCharPos(startPos);
        if (index == -1)
            throw new IllegalStateException("No identifier found");
        String text = getText();
        int length = text.length();
        int endIndex = length;
        //just in case no whitespace at end of file 
        //      _log.log("In getNextIdentifer text = \n" + text); 
        //      _log.log("index = " + index + "; length = " + length); 
        //find index of next delimiter or whitespace 
        char c;
        for (int i = index; i < length; i++) {
            c = text.charAt(i);
            if (!Character.isJavaIdentifierPart(c)) {
                endIndex = i;
                break;
            }
        }
        //      _log.log("endIndex = " + endIndex); 
        return text.substring(index, endIndex);
    } catch (BadLocationException e) {
        //      System.err.println("text =\n" + text); 
        //      System.err.println("The document =\n" + getText()); 
        //      System.err.println("startPos = " + startPos + "; length = " + length + "; index = " + index + "; endIndex = " + endIndex); 
        throw new UnexpectedException(e);
    }
}

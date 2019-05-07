/** Returns true if this position is the instantiation of an anonymous inner class.  Only runs in the event thread.
    * @param pos position of "new"
    * @param openCurlyPos position of the next '{'
    * @return true if anonymous inner class instantiation
    */
public boolean _isAnonymousInnerClass(final int pos, final int openCurlyPos) throws BadLocationException {
    //    String t = getText(0, openCurlyPos+1); 
    //    System.err.print("_isAnonymousInnerClass(" + pos + ", " + openCurlyPos + ")"); 
    //    System.err.println("_isAnonymousInnerClass(" + pos + ", " + openCurlyPos + "): `" +  
    //                       t.substring(pos, openCurlyPos+1) + "`"); 
    // Check cache 
    final Query key = new Query.AnonymousInnerClass(pos, openCurlyPos);
    Boolean cached = (Boolean) _checkCache(key);
    if (cached != null) {
        //      System.err.println(" ==> " + cached); 
        return cached;
    }
    int newPos = pos;
    //    synchronized(_reduced) { 
    cached = false;
    String text = getText(0, openCurlyPos + 1);
    // includes open Curly brace 
    newPos += "new".length();
    int classStart = getFirstNonWSCharPos(newPos);
    if (classStart != -1) {
        int classEnd = classStart + 1;
        while (classEnd < text.length()) {
            if (!Character.isJavaIdentifierPart(text.charAt(classEnd)) && text.charAt(classEnd) != '.') {
                // delimiter found 
                break;
            }
            ++classEnd;
        }
        /* Determine parenStart, the postion immediately before the open parenthesis following the superclass name. */
        //         System.err.println("\tclass = `" + text.substring(classStart,classEnd) + "`"); 
        int parenStart = getFirstNonWSCharPos(classEnd);
        if (parenStart != -1) {
            int origParenStart = parenStart;
            //           System.err.println("\tfirst non-whitespace after class = " + parenStart + " `" + text.charAt(parenStart) + "`"); 
            if (text.charAt(origParenStart) == '<') {
                parenStart = -1;
                // might be a generic class 
                int closePointyBracket = findNextEnclosingBrace(origParenStart, '<', '>');
                if (closePointyBracket != -1) {
                    if (text.charAt(closePointyBracket) == '>') {
                        parenStart = getFirstNonWSCharPos(closePointyBracket + 1);
                    }
                }
            }
        }
        if (parenStart != -1) {
            if (text.charAt(parenStart) == '(') {
                setCurrentLocation(parenStart + 1);
                // reduced model points to pos == parenStart + 1 
                int parenEnd = balanceForward();
                if (parenEnd > -1) {
                    parenEnd = parenEnd + parenStart + 1;
                    //               System.err.println("\tafter closing paren = " + parenEnd); 
                    int afterParen = getFirstNonWSCharPos(parenEnd);
                    //               System.err.println("\tfirst non-whitespace after paren = " + parenStart + " `" + text.charAt(afterParen) + "`"); 
                    cached = (afterParen == openCurlyPos);
                }
            }
        }
    }
    _storeInCache(key, cached, openCurlyPos);
    //      System.err.println(" ==> " + cached); 
    return cached;
}

/** Returns the index of the anonymous inner class being instantiated at the specified position (where openining brace
    * for anonymous inner class is pos).  Only runs in event thread.
    * @param pos is position of the opening curly brace of the anonymous inner class
    * @return anonymous class index
    */
int _getAnonymousInnerClassIndex(final int pos) throws BadLocationException, ClassNameNotFoundException {
    //    boolean oldLog = true; // log; log = false; 
    /* */
    assert Utilities.TEST_MODE || EventQueue.isDispatchThread();
    // Check cache 
    final Query key = new Query.AnonymousInnerClassIndex(pos);
    final Integer cached = (Integer) _checkCache(key);
    if (cached != null) {
        //      log = oldLog; 
        return cached.intValue();
    }
    int newPos = pos;
    // formerly pos -1 // move outside the curly brace?  Corrected to do nothing since already outside 
    //    final char[] delims = {'{','}','(',')','[',']','+','-','/','*',';',':','=','!','@','#','$','%','^','~','\\','"','`','|'}; 
    final String className = _getEnclosingClassName(newPos - 2, true);
    // class name must be followed by at least "()" 
    final String text = getText(0, newPos - 2);
    // excludes miminal (empty) argument list after class name 
    int index = 1;
    //    if (oldLog) System.err.println("anon before " + pos + " enclosed by " + className); 
    while ((newPos = _findPrevKeyword(text, "new", newPos - 4)) != -1) {
        // excludes space + minimal class name + args 
        //      if (oldLog) System.err.println("new found at " + newPos); 
        int afterNewPos = newPos + "new".length();
        int classStart = getFirstNonWSCharPos(afterNewPos);
        if (classStart == -1) {
            continue;
        }
        int classEnd = classStart + 1;
        while (classEnd < text.length()) {
            if (!Character.isJavaIdentifierPart(text.charAt(classEnd)) && text.charAt(classEnd) != '.') {
                // delimiter found 
                break;
            }
            ++classEnd;
        }
        //      if (oldLog) System.err.println("\tclass = `" + text.substring(classStart,classEnd) + "`"); 
        int parenStart = getFirstNonWSCharPos(classEnd);
        if (parenStart == -1) {
            continue;
        }
        int origParenStart = parenStart;
        //      if (oldLog) System.err.println("\tfirst non-whitespace after class = " + parenStart + " `" + text.charAt(parenStart) + "`"); 
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
        if (parenStart == -1) {
            continue;
        }
        if (text.charAt(parenStart) != '(') {
            continue;
        }
        int parenEnd = findNextEnclosingBrace(parenStart, '(', ')');
        int nextOpenCurly = _findNextOpenCurly(text, parenEnd);
        if (nextOpenCurly == -1) {
            continue;
        }
        //      if (oldLog) System.err.println("{ found at " + nextOpenCurly + ": `" +  
        //                                     text.substring(newPos, nextOpenCurly + 1) + "`"); 
        //      if (oldLog) System.err.println("_isAnonymousInnerClass(" + newPos + ", " + nextOpenCurly + ")"); 
        if (_isAnonymousInnerClass(newPos, nextOpenCurly)) {
            //        if (oldLog) System.err.println("is anonymous inner class"); 
            String cn = _getEnclosingClassName(newPos, true);
            //        if (oldLog) System.err.println("enclosing class = " + cn); 
            if (!cn.startsWith(className)) {
                break;
            } else if (!cn.equals(className)) {
                newPos = findPrevEnclosingBrace(newPos, '{', '}');
                continue;
            } else
                ++index;
        }
    }
    _storeInCache(key, index, pos);
    //    oldLog = log; 
    return index;
}

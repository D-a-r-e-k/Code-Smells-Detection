/** Searches backwards to find the name of the enclosing named class or interface. NB: ignores comments. Only runs in
    * event thread.
    * WARNING: In long source files and when contained in anonymous inner classes, this function might take a LONG time.
    * @param pos Position to start from
    * @param qual true to find the fully qualified class name
    * @return name of the enclosing named class or interface
    */
public String _getEnclosingClassName(final int pos, final boolean qual) throws BadLocationException, ClassNameNotFoundException {
    /* */
    assert Utilities.TEST_MODE || EventQueue.isDispatchThread();
    // Check cache 
    final Query key = new Query.EnclosingClassName(pos, qual);
    final String cached = (String) _checkCache(key);
    if (cached != null)
        return cached;
    final char[] delims = { '{', '}', '(', ')', '[', ']', '+', '-', '/', '*', ';', ':', '=', '!', '@', '#', '$', '%', '^', '~', '\\', '"', '`', '|' };
    String name = "";
    final String text = getText(0, pos);
    int curPos = pos;
    do {
        //        if (text.charAt(curPos) != '{' || text.charAt(curPos) != '}') ++curPos; 
        //        if (oldLog) System.err.println("curPos=" + curPos + " `" + 
        //                                       text.substring(Math.max(0,curPos-10), Math.min(text.length(), curPos+1)) + "`"); 
        curPos = findPrevEnclosingBrace(curPos, '{', '}');
        if (curPos == -1) {
            break;
        }
        int classPos = _findPrevKeyword(text, "class", curPos);
        int interPos = _findPrevKeyword(text, "interface", curPos);
        int otherPos = findPrevDelimiter(curPos, delims);
        int newPos = -1;
        // see if there's a ) closer by 
        int closeParenPos = _findPrevNonWSCharPos(curPos);
        if (closeParenPos != -1 && text.charAt(closeParenPos) == ')') {
            // yes, find the matching ( 
            int openParenPos = findPrevEnclosingBrace(closeParenPos, '(', ')');
            if (openParenPos != -1 && text.charAt(openParenPos) == '(') {
                // this might be an inner class 
                newPos = _findPrevKeyword(text, "new", openParenPos);
                //            if (oldLog) System.err.println("\tnew found at " + newPos + ", openCurlyPos=" + curPos); 
                if (!_isAnonymousInnerClass(newPos, curPos)) {
                    // not an anonymous inner class 
                    newPos = -1;
                }
            }
        }
        while (classPos != -1 || interPos != -1 || newPos != -1) {
            if (newPos != -1) {
                classPos = -1;
                interPos = -1;
                break;
            } else if (otherPos > classPos && otherPos > interPos) {
                if (text.charAt(otherPos) != '{' || text.charAt(otherPos) != '}')
                    ++otherPos;
                curPos = findPrevEnclosingBrace(otherPos, '{', '}');
                classPos = _findPrevKeyword(text, "class", curPos);
                interPos = _findPrevKeyword(text, "interface", curPos);
                otherPos = findPrevDelimiter(curPos, delims);
                newPos = -1;
                // see if there's a ) closer by 
                closeParenPos = _findPrevNonWSCharPos(curPos);
                if (closeParenPos != -1 && text.charAt(closeParenPos) == ')') {
                    // yes, find the matching ( 
                    int openParenPos = findPrevEnclosingBrace(closeParenPos, '(', ')');
                    if (openParenPos != -1 && text.charAt(openParenPos) == '(') {
                        // this might be an inner class 
                        newPos = _findPrevKeyword(text, "new", openParenPos);
                        //                if (oldLog) System.err.println("\tnew found at " + newPos + ", openCurlyPos=" + curPos); 
                        if (!_isAnonymousInnerClass(newPos, curPos))
                            newPos = -1;
                    }
                }
            } else {
                // either class or interface found first             
                curPos = Math.max(classPos, Math.max(interPos, newPos));
                break;
            }
        }
        if (classPos != -1 || interPos != -1) {
            if (classPos > interPos)
                curPos += "class".length();
            else
                curPos += "interface".length();
            // interface found first 
            int nameStart = getFirstNonWSCharPos(curPos);
            if (nameStart == -1) {
                throw new ClassNameNotFoundException("Cannot determine enclosing class name");
            }
            int nameEnd = nameStart + 1;
            while (nameEnd < text.length()) {
                if (!Character.isJavaIdentifierPart(text.charAt(nameEnd)) && text.charAt(nameEnd) != '.')
                    break;
                ++nameEnd;
            }
            name = text.substring(nameStart, nameEnd) + '$' + name;
        } else if (newPos != -1) {
            name = String.valueOf(_getAnonymousInnerClassIndex(curPos)) + "$" + name;
            curPos = newPos;
        } else
            break;
    } while (qual);
    // chop off '$' at the end. 
    if (name.length() > 0)
        name = name.substring(0, name.length() - 1);
    if (qual) {
        String pn = getPackageName();
        if ((pn.length() > 0) && (name.length() > 0)) {
            name = getPackageName() + "." + name;
        }
    }
    //    log = oldLog; 
    _storeInCache(key, name, pos);
    return name;
}

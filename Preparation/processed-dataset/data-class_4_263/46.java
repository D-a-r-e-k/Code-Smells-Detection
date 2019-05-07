//}}}  
//}}}  
//{{{ Syntax highlighting  
//{{{ markTokens() method  
/**
	 * Returns the syntax tokens for the specified line.
	 * @param lineIndex The line number
	 * @param tokenHandler The token handler that will receive the syntax
	 * tokens
	 * @since jEdit 4.1pre1
	 */
public void markTokens(int lineIndex, TokenHandler tokenHandler) {
    Segment seg = new Segment();
    if (lineIndex < 0 || lineIndex >= lineMgr.getLineCount())
        throw new ArrayIndexOutOfBoundsException(lineIndex);
    int firstInvalidLineContext = lineMgr.getFirstInvalidLineContext();
    int start;
    if (textMode || firstInvalidLineContext == -1) {
        start = lineIndex;
    } else {
        start = Math.min(firstInvalidLineContext, lineIndex);
    }
    if (Debug.TOKEN_MARKER_DEBUG)
        Log.log(Log.DEBUG, this, "tokenize from " + start + " to " + lineIndex);
    TokenMarker.LineContext oldContext = null;
    TokenMarker.LineContext context = null;
    for (int i = start; i <= lineIndex; i++) {
        getLineText(i, seg);
        oldContext = lineMgr.getLineContext(i);
        TokenMarker.LineContext prevContext = ((i == 0 || textMode) ? null : lineMgr.getLineContext(i - 1));
        context = tokenMarker.markTokens(prevContext, (i == lineIndex ? tokenHandler : DummyTokenHandler.INSTANCE), seg);
        lineMgr.setLineContext(i, context);
    }
    int lineCount = lineMgr.getLineCount();
    if (lineCount - 1 == lineIndex)
        lineMgr.setFirstInvalidLineContext(-1);
    else if (oldContext != context)
        lineMgr.setFirstInvalidLineContext(lineIndex + 1);
    else if (firstInvalidLineContext == -1)
        /* do nothing */
        ;
    else {
        lineMgr.setFirstInvalidLineContext(Math.max(firstInvalidLineContext, lineIndex + 1));
    }
}

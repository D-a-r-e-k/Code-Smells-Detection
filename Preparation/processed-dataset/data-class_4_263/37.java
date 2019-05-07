//}}}  
//{{{ getCurrentIndentForLine() method  
/**
	 * Returns the line's current leading indent.
	 * @param lineIndex The line number
	 * @param whitespaceChars If this is non-null, the number of whitespace
	 * characters is stored at the 0 index
	 * @since jEdit 4.2pre2
	 */
public int getCurrentIndentForLine(int lineIndex, int[] whitespaceChars) {
    Segment seg = new Segment();
    getLineText(lineIndex, seg);
    int tabSize = getTabSize();
    int currentIndent = 0;
    loop: for (int i = 0; i < seg.count; i++) {
        char c = seg.array[seg.offset + i];
        switch(c) {
            case ' ':
                currentIndent++;
                if (whitespaceChars != null)
                    whitespaceChars[0]++;
                break;
            case '\t':
                currentIndent += tabSize - (currentIndent % tabSize);
                if (whitespaceChars != null)
                    whitespaceChars[0]++;
                break;
            default:
                break loop;
        }
    }
    return currentIndent;
}

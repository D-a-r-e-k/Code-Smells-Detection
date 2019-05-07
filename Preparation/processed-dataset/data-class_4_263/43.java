//}}}  
//{{{ insertIndented() method  
/**
	 * Inserts a string into the buffer, indenting each line of the string
	 * to match the indent of the first line.
	 *
	 * @param offset The offset
	 * @param text The text
	 *
	 * @return The number of characters of indent inserted on each new
	 * line. This is used by the abbreviations code.
	 *
	 * @since jEdit 4.2pre14
	 */
public int insertIndented(int offset, String text) {
    try {
        beginCompoundEdit();
        // obtain the leading indent for later use  
        int firstLine = getLineOfOffset(offset);
        CharSequence lineText = getLineSegment(firstLine);
        int leadingIndent = StandardUtilities.getLeadingWhiteSpaceWidth(lineText, getTabSize());
        String whiteSpace = StandardUtilities.createWhiteSpace(leadingIndent, getBooleanProperty("noTabs") ? 0 : getTabSize());
        insert(offset, text);
        int lastLine = getLineOfOffset(offset + text.length());
        // note that if firstLine == lastLine, loop does not  
        // execute  
        for (int i = firstLine + 1; i <= lastLine; i++) {
            insert(getLineStartOffset(i), whiteSpace);
        }
        return whiteSpace.length();
    } finally {
        endCompoundEdit();
    }
}

//}}}  
//{{{ shiftIndentLeft() method  
/**
	 * Shifts the indent of each line in the specified list to the left.
	 * @param lines The line numbers
	 * @since jEdit 3.2pre1
	 */
public void shiftIndentLeft(int[] lines) {
    int tabSize = getTabSize();
    int indentSize = getIndentSize();
    boolean noTabs = getBooleanProperty("noTabs");
    try {
        beginCompoundEdit();
        for (int i = 0; i < lines.length; i++) {
            int lineStart = getLineStartOffset(lines[i]);
            CharSequence line = getLineSegment(lines[i]);
            int whiteSpace = StandardUtilities.getLeadingWhiteSpace(line);
            if (whiteSpace == 0)
                continue;
            int whiteSpaceWidth = Math.max(0, StandardUtilities.getLeadingWhiteSpaceWidth(line, tabSize) - indentSize);
            insert(lineStart + whiteSpace, StandardUtilities.createWhiteSpace(whiteSpaceWidth, noTabs ? 0 : tabSize));
            remove(lineStart, whiteSpace);
        }
    } finally {
        endCompoundEdit();
    }
}

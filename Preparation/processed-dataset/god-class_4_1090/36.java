/**
	 * Indents the specified line.
	 * @param lineIndex The line number to indent
	 * @param canDecreaseIndent If true, the indent can be decreased as a
	 * result of this. Set this to false for Tab key.
	 * @return true If indentation took place, false otherwise.
	 * @since jEdit 4.2pre2
	 */
public boolean indentLine(int lineIndex, boolean canDecreaseIndent) {
    int[] whitespaceChars = new int[1];
    int currentIndent = getCurrentIndentForLine(lineIndex, whitespaceChars);
    int prevLineIndex = getPriorNonEmptyLine(lineIndex);
    int prevLineIndent = (prevLineIndex == -1) ? 0 : StandardUtilities.getLeadingWhiteSpaceWidth(getLineSegment(prevLineIndex), getTabSize());
    int idealIndent = getIdealIndentForLine(lineIndex, prevLineIndex, prevLineIndent);
    if (idealIndent == -1 || idealIndent == currentIndent || (!canDecreaseIndent && idealIndent < currentIndent))
        return false;
    // Do it  
    try {
        beginCompoundEdit();
        int start = getLineStartOffset(lineIndex);
        remove(start, whitespaceChars[0]);
        String prevIndentString = (prevLineIndex >= 0) ? StandardUtilities.getIndentString(getLineText(prevLineIndex)) : null;
        String indentString;
        if (prevIndentString == null) {
            indentString = StandardUtilities.createWhiteSpace(idealIndent, getBooleanProperty("noTabs") ? 0 : getTabSize());
        } else if (idealIndent == prevLineIndent)
            indentString = prevIndentString;
        else if (idealIndent < prevLineIndent)
            indentString = StandardUtilities.truncateWhiteSpace(idealIndent, getTabSize(), prevIndentString);
        else
            indentString = prevIndentString + StandardUtilities.createWhiteSpace(idealIndent - prevLineIndent, getBooleanProperty("noTabs") ? 0 : getTabSize(), prevLineIndent);
        insert(start, indentString);
    } finally {
        endCompoundEdit();
    }
    return true;
}

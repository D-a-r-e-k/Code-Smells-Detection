//}}}  
//{{{ getIdealIndentForLine() method  
/**
	 * Returns the ideal leading indent for the specified line.
	 * This will apply the various auto-indent rules.
	 * @param lineIndex The line number
	 */
public int getIdealIndentForLine(int lineIndex) {
    int prevLineIndex = getPriorNonEmptyLine(lineIndex);
    int oldIndent = prevLineIndex == -1 ? 0 : StandardUtilities.getLeadingWhiteSpaceWidth(getLineSegment(prevLineIndex), getTabSize());
    return getIdealIndentForLine(lineIndex, prevLineIndex, oldIndent);
}

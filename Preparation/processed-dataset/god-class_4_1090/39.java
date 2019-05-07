//}}}  
//{{{ getIdealIndentForLine() method  
/**
	 * Returns the ideal leading indent for the specified line.
	 * This will apply the various auto-indent rules.
	 * @param lineIndex The line number
	 * @param prevLineIndex The index of the previous non-empty line
	 * @param oldIndent The indent width of the previous line (or 0)
	 */
private int getIdealIndentForLine(int lineIndex, int prevLineIndex, int oldIndent) {
    int prevPrevLineIndex = prevLineIndex < 0 ? -1 : getPriorNonEmptyLine(prevLineIndex);
    int newIndent = oldIndent;
    List<IndentRule> indentRules = getIndentRules(lineIndex);
    List<IndentAction> actions = new LinkedList<IndentAction>();
    for (int i = 0; i < indentRules.size(); i++) {
        IndentRule rule = indentRules.get(i);
        rule.apply(this, lineIndex, prevLineIndex, prevPrevLineIndex, actions);
    }
    for (IndentAction action : actions) {
        newIndent = action.calculateIndent(this, lineIndex, oldIndent, newIndent);
        if (!action.keepChecking())
            break;
    }
    if (newIndent < 0)
        newIndent = 0;
    return newIndent;
}

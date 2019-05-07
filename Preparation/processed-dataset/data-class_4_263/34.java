/**
	 * Indents all specified lines.
	 * @param lines The line numbers
	 * @since jEdit 3.2pre1
	 */
public void indentLines(int[] lines) {
    try {
        beginCompoundEdit();
        for (int i = 0; i < lines.length; i++) indentLine(lines[i], true);
    } finally {
        endCompoundEdit();
    }
}

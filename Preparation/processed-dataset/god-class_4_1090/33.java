//}}}  
//{{{ indentLines() methods  
/**
	 * Indents all specified lines.
	 * @param start The first line to indent
	 * @param end The last line to indent
	 * @since jEdit 3.1pre3
	 */
public void indentLines(int start, int end) {
    try {
        beginCompoundEdit();
        for (int i = start; i <= end; i++) indentLine(i, true);
    } finally {
        endCompoundEdit();
    }
}

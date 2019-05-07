//}}}  
//}}}  
//{{{ Indentation  
//{{{ removeTrailingWhiteSpace() method  
/**
	 * Removes trailing whitespace from all lines in the specified list.
	 * @param lines The line numbers
	 * @since jEdit 3.2pre1
	 */
public void removeTrailingWhiteSpace(int[] lines) {
    try {
        beginCompoundEdit();
        for (int i = 0; i < lines.length; i++) {
            int pos, lineStart, lineEnd, tail;
            Segment seg = new Segment();
            getLineText(lines[i], seg);
            // blank line  
            if (seg.count == 0)
                continue;
            lineStart = seg.offset;
            lineEnd = seg.offset + seg.count - 1;
            for (pos = lineEnd; pos >= lineStart; pos--) {
                if (!Character.isWhitespace(seg.array[pos]))
                    break;
            }
            tail = lineEnd - pos;
            // no whitespace  
            if (tail == 0)
                continue;
            remove(getLineEndOffset(lines[i]) - 1 - tail, tail);
        }
    } finally {
        endCompoundEdit();
    }
}

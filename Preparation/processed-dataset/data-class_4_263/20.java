//}}}  
//{{{ getPriorNonEmptyLine() method  
/**
	 * Auto indent needs this.
	 */
public int getPriorNonEmptyLine(int lineIndex) {
    int returnValue = -1;
    if (!mode.getIgnoreWhitespace()) {
        return lineIndex - 1;
    }
    for (int i = lineIndex - 1; i >= 0; i--) {
        Segment seg = new Segment();
        getLineText(i, seg);
        if (seg.count != 0)
            returnValue = i;
        for (int j = 0; j < seg.count; j++) {
            char ch = seg.array[seg.offset + j];
            if (!Character.isWhitespace(ch))
                return i;
        }
    }
    // didn't find a line that contains non-whitespace chars  
    // so return index of prior whitespace line  
    return returnValue;
}

//}}}  
//{{{ getFoldAtLine() method  
/**
	 * Returns an array. The first element is the start line, the
	 * second element is the end line, of the fold containing the
	 * specified line number.
	 * @param line The line number
	 * @since jEdit 4.0pre3
	 */
public int[] getFoldAtLine(int line) {
    int start, end;
    if (isFoldStart(line)) {
        start = line;
        int foldLevel = getFoldLevel(line);
        line++;
        while (getFoldLevel(line) > foldLevel) {
            line++;
            if (line == getLineCount())
                break;
        }
        end = line - 1;
    } else {
        start = line;
        int foldLevel = getFoldLevel(line);
        while (getFoldLevel(start) >= foldLevel) {
            if (start == 0)
                break;
            else
                start--;
        }
        end = line;
        while (getFoldLevel(end) >= foldLevel) {
            end++;
            if (end == getLineCount())
                break;
        }
        end--;
    }
    while (getLineLength(end) == 0 && end > start) end--;
    return new int[] { start, end };
}

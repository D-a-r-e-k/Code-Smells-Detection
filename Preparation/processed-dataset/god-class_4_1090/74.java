//}}}  
//{{{ isFoldEnd() method  
/**
	 * Returns if the specified line ends a fold.
	 * @since jEdit 4.2pre5
	 */
public boolean isFoldEnd(int line) {
    return line != getLineCount() - 1 && getFoldLevel(line) > getFoldLevel(line + 1);
}

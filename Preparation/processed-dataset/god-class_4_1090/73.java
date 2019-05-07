//}}}  
//}}}  
//{{{ Folding methods  
//{{{ isFoldStart() method  
/**
	 * Returns if the specified line begins a fold.
	 * @since jEdit 3.1pre1
	 */
public boolean isFoldStart(int line) {
    return line != getLineCount() - 1 && getFoldLevel(line) < getFoldLevel(line + 1);
}

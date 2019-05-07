//}}}  
//{{{ indentLine() methods  
/**
	 * @deprecated Use {@link #indentLine(int,boolean)} instead.
	 */
@Deprecated
public boolean indentLine(int lineIndex, boolean canIncreaseIndent, boolean canDecreaseIndent) {
    return indentLine(lineIndex, canDecreaseIndent);
}

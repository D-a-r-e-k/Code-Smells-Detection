//}}}  
//{{{ getKeywordMapAtOffset() method  
/**
	 * Returns the syntax highlighting keyword map in effect at the
	 * specified offset. Used by the <b>Complete Word</b> command to
	 * complete keywords.
	 * @param offset The offset
	 * @since jEdit 4.0pre3
	 */
public KeywordMap getKeywordMapAtOffset(int offset) {
    return getRuleSetAtOffset(offset).getKeywords();
}

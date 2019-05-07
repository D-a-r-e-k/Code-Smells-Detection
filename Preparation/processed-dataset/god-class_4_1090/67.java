//}}}  
//{{{ getRuleSetAtOffset() method  
/**
	 * Returns the syntax highlighting ruleset at the specified offset.
	 * @since jEdit 4.1pre1
	 */
public ParserRuleSet getRuleSetAtOffset(int offset) {
    int line = getLineOfOffset(offset);
    offset -= getLineStartOffset(line);
    if (offset != 0)
        offset--;
    DefaultTokenHandler tokens = new DefaultTokenHandler();
    markTokens(line, tokens);
    Token token = TextUtilities.getTokenAtOffset(tokens.getTokens(), offset);
    return token.rules;
}

/**
	 * @deprecated Use the other form of <code>markTokens()</code> instead
	 */
@Deprecated
public TokenList markTokens(int lineIndex) {
    TokenList list = new TokenList();
    markTokens(lineIndex, list);
    return list;
}

//}}}  
//{{{ editSyntaxStyle() method  
/**
	 * Edit the syntax style of the token under the caret.
	 *
	 * @param textArea the textarea where your caret is
	 * @since jEdit 4.3pre11
	 */
public void editSyntaxStyle(JEditTextArea textArea) {
    int lineNum = textArea.getCaretLine();
    int start = getLineStartOffset(lineNum);
    int position = textArea.getCaretPosition();
    DefaultTokenHandler tokenHandler = new DefaultTokenHandler();
    markTokens(lineNum, tokenHandler);
    Token token = tokenHandler.getTokens();
    while (token.id != Token.END) {
        int next = start + token.length;
        if (start <= position && next > position)
            break;
        start = next;
        token = token.next;
    }
    if (token.id == Token.END || token.id == Token.NULL) {
        JOptionPane.showMessageDialog(jEdit.getActiveView(), jEdit.getProperty("syntax-style-no-token.message"), jEdit.getProperty("syntax-style-no-token.title"), JOptionPane.PLAIN_MESSAGE);
        return;
    }
    String typeName = Token.tokenToString(token.id);
    String property = "view.style." + typeName.toLowerCase();
    SyntaxStyle currentStyle = GUIUtilities.parseStyle(jEdit.getProperty(property), "Dialog", 12);
    SyntaxStyle style = new StyleEditor(jEdit.getActiveView(), currentStyle, typeName).getStyle();
    if (style != null) {
        jEdit.setProperty(property, GUIUtilities.getStyleString(style));
        jEdit.propertiesChanged();
    }
}

public void setText(char[] c, int off, int len) {
    if (tagName.equals("EOL_SPAN") || tagName.equals("EOL_SPAN_REGEXP") || tagName.equals("MARK_PREVIOUS") || tagName.equals("MARK_FOLLOWING") || tagName.equals("SEQ") || tagName.equals("SEQ_REGEXP") || tagName.equals("BEGIN")) {
        TagDecl target = this;
        if (tagName.equals("BEGIN"))
            target = stateStack.get(stateStack.size() - 2);
        if (target.lastStart == null) {
            target.lastStart = new StringBuffer();
            target.lastStart.append(c, off, len);
            target.lastStartPosMatch = ((target.lastAtLineStart ? ParserRule.AT_LINE_START : 0) | (target.lastAtWhitespaceEnd ? ParserRule.AT_WHITESPACE_END : 0) | (target.lastAtWordStart ? ParserRule.AT_WORD_START : 0));
            target.lastAtLineStart = false;
            target.lastAtWordStart = false;
            target.lastAtWhitespaceEnd = false;
        } else {
            target.lastStart.append(c, off, len);
        }
    } else if (tagName.equals("END")) {
        TagDecl target = stateStack.get(stateStack.size() - 2);
        if (target.lastEnd == null) {
            target.lastEnd = new StringBuffer();
            target.lastEnd.append(c, off, len);
            target.lastEndPosMatch = ((this.lastAtLineStart ? ParserRule.AT_LINE_START : 0) | (this.lastAtWhitespaceEnd ? ParserRule.AT_WHITESPACE_END : 0) | (this.lastAtWordStart ? ParserRule.AT_WORD_START : 0));
            target.lastAtLineStart = false;
            target.lastAtWordStart = false;
            target.lastAtWhitespaceEnd = false;
        } else {
            target.lastEnd.append(c, off, len);
        }
    } else {
        if (lastKeyword == null)
            lastKeyword = new StringBuffer();
        lastKeyword.append(c, off, len);
    }
}

public static Object case22_line374(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (support.isInDef() || support.isInSingle()) {
        support.warn(ID.END_IN_METHOD, ((Token) yyVals[-3 + yyTop]).getPosition(), "END in method; use at_exit");
    }
    yyVal = new PostExeNode(((Token) yyVals[-3 + yyTop]).getPosition(), ((Node) yyVals[-1 + yyTop]));
    return yyVal;
}

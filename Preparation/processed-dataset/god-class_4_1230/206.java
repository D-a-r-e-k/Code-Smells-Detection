public static Object case428_line1647(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = lexer.getStrTerm();
    lexer.setStrTerm(null);
    lexer.setState(LexState.EXPR_BEG);
    return yyVal;
}

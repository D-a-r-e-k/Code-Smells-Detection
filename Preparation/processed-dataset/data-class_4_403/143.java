public static Object case308_line1188(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    support.setInSingle(support.getInSingle() + 1);
    support.pushLocalScope();
    lexer.setState(LexState.EXPR_END);
    /* force for args */
    return yyVal;
}

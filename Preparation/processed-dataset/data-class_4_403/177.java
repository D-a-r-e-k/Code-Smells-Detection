public static Object case434_line1669(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    lexer.setState(LexState.EXPR_END);
    yyVal = ((Token) yyVals[0 + yyTop]);
    ((ISourcePositionHolder) yyVal).setPosition(((Token) yyVals[-1 + yyTop]).getPosition());
    return yyVal;
}

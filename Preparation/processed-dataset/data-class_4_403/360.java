public static Object case464_line1771(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = ((ArgsNode) yyVals[-1 + yyTop]);
    ((ISourcePositionHolder) yyVal).setPosition(((Token) yyVals[-2 + yyTop]).getPosition());
    lexer.setState(LexState.EXPR_BEG);
    return yyVal;
}

public static Object case418_line1610(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = ((ListNode) yyVals[-1 + yyTop]);
    ((ISourcePositionHolder) yyVal).setPosition(((Token) yyVals[-2 + yyTop]).getPosition());
    return yyVal;
}

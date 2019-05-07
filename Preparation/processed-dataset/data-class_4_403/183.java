public static Object case234_line923(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = ((Node) yyVals[-1 + yyTop]);
    if (yyVal != null)
        ((Node) yyVal).setPosition(((Token) yyVals[-2 + yyTop]).getPosition());
    return yyVal;
}

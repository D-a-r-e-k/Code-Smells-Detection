public static Object case268_line1037(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    support.warning(ID.GROUPED_EXPRESSION, ((Token) yyVals[-3 + yyTop]).getPosition(), "(...) interpreted as grouped expression");
    yyVal = ((Node) yyVals[-2 + yyTop]);
    return yyVal;
}

public static Object case24_line384(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    support.checkExpression(((Node) yyVals[0 + yyTop]));
    ((MultipleAsgn19Node) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
    yyVal = ((MultipleAsgn19Node) yyVals[-2 + yyTop]);
    return yyVal;
}

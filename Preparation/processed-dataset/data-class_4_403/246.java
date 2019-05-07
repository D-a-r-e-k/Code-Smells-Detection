public static Object case508_line1958(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (((Node) yyVals[-1 + yyTop]) == null) {
        support.yyerror("can't define single method for ().");
    } else if (((Node) yyVals[-1 + yyTop]) instanceof ILiteralNode) {
        support.yyerror("can't define single method for literals.");
    }
    support.checkExpression(((Node) yyVals[-1 + yyTop]));
    yyVal = ((Node) yyVals[-1 + yyTop]);
    return yyVal;
}

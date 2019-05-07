public static Object case195_line789(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    support.checkExpression(((Node) yyVals[-2 + yyTop]));
    support.checkExpression(((Node) yyVals[0 + yyTop]));
    boolean isLiteral = ((Node) yyVals[-2 + yyTop]) instanceof FixnumNode && ((Node) yyVals[0 + yyTop]) instanceof FixnumNode;
    yyVal = new DotNode(support.getPosition(((Node) yyVals[-2 + yyTop])), ((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]), false, isLiteral);
    return yyVal;
}

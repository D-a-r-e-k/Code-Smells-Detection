public static Object case321_line1227(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new IfNode(((Token) yyVals[-4 + yyTop]).getPosition(), support.getConditionNode(((Node) yyVals[-3 + yyTop])), ((Node) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]));
    return yyVal;
}

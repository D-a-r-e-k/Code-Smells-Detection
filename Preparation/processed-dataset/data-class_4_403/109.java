public static Object case286_line1107(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new IfNode(((Token) yyVals[-5 + yyTop]).getPosition(), support.getConditionNode(((Node) yyVals[-4 + yyTop])), ((Node) yyVals[-1 + yyTop]), ((Node) yyVals[-2 + yyTop]));
    return yyVal;
}

public static Object case15_line340(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new IfNode(support.getPosition(((Node) yyVals[-2 + yyTop])), support.getConditionNode(((Node) yyVals[0 + yyTop])), ((Node) yyVals[-2 + yyTop]), null);
    return yyVal;
}

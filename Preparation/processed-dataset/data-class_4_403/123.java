public static Object case289_line1114(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    Node body = ((Node) yyVals[-1 + yyTop]) == null ? NilImplicitNode.NIL : ((Node) yyVals[-1 + yyTop]);
    yyVal = new WhileNode(((Token) yyVals[-6 + yyTop]).getPosition(), support.getConditionNode(((Node) yyVals[-4 + yyTop])), body);
    return yyVal;
}

public static Object case292_line1122(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    Node body = ((Node) yyVals[-1 + yyTop]) == null ? NilImplicitNode.NIL : ((Node) yyVals[-1 + yyTop]);
    yyVal = new UntilNode(((Token) yyVals[-6 + yyTop]).getPosition(), support.getConditionNode(((Node) yyVals[-4 + yyTop])), body);
    return yyVal;
}

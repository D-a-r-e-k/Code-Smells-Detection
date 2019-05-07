public static Object case19_line360(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    Node body = ((Node) yyVals[0 + yyTop]) == null ? NilImplicitNode.NIL : ((Node) yyVals[0 + yyTop]);
    yyVal = new RescueNode(support.getPosition(((Node) yyVals[-2 + yyTop])), ((Node) yyVals[-2 + yyTop]), new RescueBodyNode(support.getPosition(((Node) yyVals[-2 + yyTop])), null, body, null), null);
    return yyVal;
}

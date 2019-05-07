public static Object case185_line725(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    ISourcePosition position = ((Token) yyVals[-1 + yyTop]).getPosition();
    Node body = ((Node) yyVals[0 + yyTop]) == null ? NilImplicitNode.NIL : ((Node) yyVals[0 + yyTop]);
    yyVal = support.node_assign(((Node) yyVals[-4 + yyTop]), new RescueNode(position, ((Node) yyVals[-2 + yyTop]), new RescueBodyNode(position, null, body, null), null));
    return yyVal;
}

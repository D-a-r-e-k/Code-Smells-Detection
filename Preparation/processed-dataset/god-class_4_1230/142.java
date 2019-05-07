public static Object case3_line288(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    Node node = ((Node) yyVals[-3 + yyTop]);
    if (((RescueBodyNode) yyVals[-2 + yyTop]) != null) {
        node = new RescueNode(support.getPosition(((Node) yyVals[-3 + yyTop])), ((Node) yyVals[-3 + yyTop]), ((RescueBodyNode) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]));
    } else if (((Node) yyVals[-1 + yyTop]) != null) {
        support.warn(ID.ELSE_WITHOUT_RESCUE, support.getPosition(((Node) yyVals[-3 + yyTop])), "else without rescue is useless");
        node = support.appendToBlock(((Node) yyVals[-3 + yyTop]), ((Node) yyVals[-1 + yyTop]));
    }
    if (((Node) yyVals[0 + yyTop]) != null) {
        if (node == null)
            node = NilImplicitNode.NIL;
        node = new EnsureNode(support.getPosition(((Node) yyVals[-3 + yyTop])), node, ((Node) yyVals[0 + yyTop]));
    }
    yyVal = node;
    return yyVal;
}

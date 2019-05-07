public static Object case392_line1475(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    Node node;
    if (((Node) yyVals[-3 + yyTop]) != null) {
        node = support.appendToBlock(support.node_assign(((Node) yyVals[-3 + yyTop]), new GlobalVarNode(((Token) yyVals[-5 + yyTop]).getPosition(), "$!")), ((Node) yyVals[-1 + yyTop]));
        if (((Node) yyVals[-1 + yyTop]) != null) {
            node.setPosition(support.unwrapNewlineNode(((Node) yyVals[-1 + yyTop])).getPosition());
        }
    } else {
        node = ((Node) yyVals[-1 + yyTop]);
    }
    Node body = node == null ? NilImplicitNode.NIL : node;
    yyVal = new RescueBodyNode(((Token) yyVals[-5 + yyTop]).getPosition(), ((Node) yyVals[-4 + yyTop]), body, ((RescueBodyNode) yyVals[0 + yyTop]));
    return yyVal;
}

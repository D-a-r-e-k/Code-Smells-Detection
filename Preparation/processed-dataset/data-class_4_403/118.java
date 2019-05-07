public static Object case187_line747(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    support.checkExpression(((Node) yyVals[-2 + yyTop]));
    ISourcePosition pos = ((Token) yyVals[-1 + yyTop]).getPosition();
    Node body = ((Node) yyVals[0 + yyTop]) == null ? NilImplicitNode.NIL : ((Node) yyVals[0 + yyTop]);
    Node rescueNode = new RescueNode(pos, ((Node) yyVals[-2 + yyTop]), new RescueBodyNode(pos, null, body, null), null);
    pos = ((AssignableNode) yyVals[-4 + yyTop]).getPosition();
    String asgnOp = (String) ((Token) yyVals[-3 + yyTop]).getValue();
    if (asgnOp.equals("||")) {
        ((AssignableNode) yyVals[-4 + yyTop]).setValueNode(((Node) yyVals[-2 + yyTop]));
        yyVal = new OpAsgnOrNode(pos, support.gettable2(((AssignableNode) yyVals[-4 + yyTop])), ((AssignableNode) yyVals[-4 + yyTop]));
    } else if (asgnOp.equals("&&")) {
        ((AssignableNode) yyVals[-4 + yyTop]).setValueNode(((Node) yyVals[-2 + yyTop]));
        yyVal = new OpAsgnAndNode(pos, support.gettable2(((AssignableNode) yyVals[-4 + yyTop])), ((AssignableNode) yyVals[-4 + yyTop]));
    } else {
        ((AssignableNode) yyVals[-4 + yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(((AssignableNode) yyVals[-4 + yyTop])), asgnOp, ((Node) yyVals[-2 + yyTop])));
        ((AssignableNode) yyVals[-4 + yyTop]).setPosition(pos);
        yyVal = ((AssignableNode) yyVals[-4 + yyTop]);
    }
    return yyVal;
}

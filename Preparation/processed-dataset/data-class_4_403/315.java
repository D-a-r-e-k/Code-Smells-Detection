public static Object case186_line730(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    support.checkExpression(((Node) yyVals[0 + yyTop]));
    ISourcePosition pos = ((AssignableNode) yyVals[-2 + yyTop]).getPosition();
    String asgnOp = (String) ((Token) yyVals[-1 + yyTop]).getValue();
    if (asgnOp.equals("||")) {
        ((AssignableNode) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
        yyVal = new OpAsgnOrNode(pos, support.gettable2(((AssignableNode) yyVals[-2 + yyTop])), ((AssignableNode) yyVals[-2 + yyTop]));
    } else if (asgnOp.equals("&&")) {
        ((AssignableNode) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
        yyVal = new OpAsgnAndNode(pos, support.gettable2(((AssignableNode) yyVals[-2 + yyTop])), ((AssignableNode) yyVals[-2 + yyTop]));
    } else {
        ((AssignableNode) yyVals[-2 + yyTop]).setValueNode(support.getOperatorCallNode(support.gettable2(((AssignableNode) yyVals[-2 + yyTop])), asgnOp, ((Node) yyVals[0 + yyTop])));
        ((AssignableNode) yyVals[-2 + yyTop]).setPosition(pos);
        yyVal = ((AssignableNode) yyVals[-2 + yyTop]);
    }
    return yyVal;
}

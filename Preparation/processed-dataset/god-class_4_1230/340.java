public static Object case17_line346(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (((Node) yyVals[-2 + yyTop]) != null && ((Node) yyVals[-2 + yyTop]) instanceof BeginNode) {
        yyVal = new WhileNode(support.getPosition(((Node) yyVals[-2 + yyTop])), support.getConditionNode(((Node) yyVals[0 + yyTop])), ((BeginNode) yyVals[-2 + yyTop]).getBodyNode(), false);
    } else {
        yyVal = new WhileNode(support.getPosition(((Node) yyVals[-2 + yyTop])), support.getConditionNode(((Node) yyVals[0 + yyTop])), ((Node) yyVals[-2 + yyTop]), true);
    }
    return yyVal;
}

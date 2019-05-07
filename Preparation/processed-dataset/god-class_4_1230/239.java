public static Object case204_line824(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = support.getOperatorCallNode(support.getOperatorCallNode(((FloatNode) yyVals[-2 + yyTop]), "**", ((Node) yyVals[0 + yyTop]), lexer.getPosition()), "-@");
    return yyVal;
}

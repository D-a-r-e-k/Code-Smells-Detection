public static Object case203_line821(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = support.getOperatorCallNode(support.getOperatorCallNode(((Node) yyVals[-2 + yyTop]), "**", ((Node) yyVals[0 + yyTop]), lexer.getPosition()), "-@");
    return yyVal;
}

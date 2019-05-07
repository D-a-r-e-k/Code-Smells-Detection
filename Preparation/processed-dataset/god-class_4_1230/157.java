public static Object case16_line343(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new IfNode(support.getPosition(((Node) yyVals[-2 + yyTop])), support.getConditionNode(((Node) yyVals[0 + yyTop])), null, ((Node) yyVals[-2 + yyTop]));
    return yyVal;
}

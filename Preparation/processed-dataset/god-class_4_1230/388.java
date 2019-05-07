public static Object case366_line1375(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new LambdaNode(((ArgsNode) yyVals[-1 + yyTop]).getPosition(), ((ArgsNode) yyVals[-1 + yyTop]), ((Node) yyVals[0 + yyTop]), support.getCurrentScope());
    support.popCurrentScope();
    lexer.setLeftParenBegin(((Integer) yyVals[-2 + yyTop]));
    return yyVal;
}

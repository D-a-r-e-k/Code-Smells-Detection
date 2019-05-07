public static Object case372_line1398(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new IterNode(support.getPosition(((Token) yyVals[-4 + yyTop])), ((ArgsNode) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]), support.getCurrentScope());
    support.popCurrentScope();
    return yyVal;
}

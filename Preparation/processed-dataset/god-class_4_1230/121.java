public static Object case386_line1456(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new IterNode(((Token) yyVals[-4 + yyTop]).getPosition(), ((ArgsNode) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]), support.getCurrentScope());
    support.popCurrentScope();
    return yyVal;
}

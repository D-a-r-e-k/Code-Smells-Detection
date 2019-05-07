public static Object case388_line1462(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new IterNode(((Token) yyVals[-4 + yyTop]).getPosition(), ((ArgsNode) yyVals[-2 + yyTop]), ((Node) yyVals[-1 + yyTop]), support.getCurrentScope());
    /* FIXME: What the hell is this?*/
    ((ISourcePositionHolder) yyVals[-5 + yyTop]).setPosition(support.getPosition(((ISourcePositionHolder) yyVals[-5 + yyTop])));
    support.popCurrentScope();
    return yyVal;
}

public static Object case21_line369(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    support.getResult().addBeginNode(new PreExeNode(((Token) yyVals[-4 + yyTop]).getPosition(), support.getCurrentScope(), ((Node) yyVals[-1 + yyTop])));
    support.popCurrentScope();
    yyVal = null;
    return yyVal;
}

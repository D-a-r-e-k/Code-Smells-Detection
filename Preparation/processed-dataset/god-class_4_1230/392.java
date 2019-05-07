public static Object case304_line1169(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    Node body = ((Node) yyVals[-1 + yyTop]) == null ? NilImplicitNode.NIL : ((Node) yyVals[-1 + yyTop]);
    yyVal = new ModuleNode(((Token) yyVals[-4 + yyTop]).getPosition(), ((Colon3Node) yyVals[-3 + yyTop]), support.getCurrentScope(), body);
    support.popCurrentScope();
    return yyVal;
}

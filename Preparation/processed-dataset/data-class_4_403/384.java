public static Object case299_line1145(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    Node body = ((Node) yyVals[-1 + yyTop]) == null ? NilImplicitNode.NIL : ((Node) yyVals[-1 + yyTop]);
    yyVal = new ClassNode(((Token) yyVals[-5 + yyTop]).getPosition(), ((Colon3Node) yyVals[-4 + yyTop]), support.getCurrentScope(), body, ((Node) yyVals[-3 + yyTop]));
    support.popCurrentScope();
    return yyVal;
}

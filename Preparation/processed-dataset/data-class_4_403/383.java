public static Object case302_line1158(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new SClassNode(((Token) yyVals[-7 + yyTop]).getPosition(), ((Node) yyVals[-5 + yyTop]), support.getCurrentScope(), ((Node) yyVals[-1 + yyTop]));
    support.popCurrentScope();
    support.setInDef(((Boolean) yyVals[-4 + yyTop]).booleanValue());
    support.setInSingle(((Integer) yyVals[-2 + yyTop]).intValue());
    return yyVal;
}

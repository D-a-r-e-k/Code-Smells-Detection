public static Object case27_line410(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new OpAsgnNode(support.getPosition(((Node) yyVals[-4 + yyTop])), ((Node) yyVals[-4 + yyTop]), ((Node) yyVals[0 + yyTop]), (String) ((Token) yyVals[-2 + yyTop]).getValue(), (String) ((Token) yyVals[-1 + yyTop]).getValue());
    return yyVal;
}

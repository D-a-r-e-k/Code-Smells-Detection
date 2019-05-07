public static Object case405_line1532(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = new StrNode(((Token) yyVals[-1 + yyTop]).getPosition(), ByteList.create((String) ((Token) yyVals[0 + yyTop]).getValue()));
    return yyVal;
}

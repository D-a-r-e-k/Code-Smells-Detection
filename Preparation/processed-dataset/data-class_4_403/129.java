public static Object case226_line896(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* ENEBO: arg surrounded by in_defined set/unset*/
    yyVal = new DefinedNode(((Token) yyVals[-2 + yyTop]).getPosition(), ((Node) yyVals[0 + yyTop]));
    return yyVal;
}

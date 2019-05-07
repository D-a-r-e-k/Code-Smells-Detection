public static Object case269_line1041(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (((Node) yyVals[-1 + yyTop]) != null) {
        /* compstmt position includes both parens around it*/
        ((ISourcePositionHolder) ((Node) yyVals[-1 + yyTop])).setPosition(((Token) yyVals[-2 + yyTop]).getPosition());
        yyVal = ((Node) yyVals[-1 + yyTop]);
    } else {
        yyVal = new NilNode(((Token) yyVals[-2 + yyTop]).getPosition());
    }
    return yyVal;
}

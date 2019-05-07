public static Object case408_line1542(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = ((Node) yyVals[-1 + yyTop]);
    ((ISourcePositionHolder) yyVal).setPosition(((Token) yyVals[-2 + yyTop]).getPosition());
    int extraLength = ((String) ((Token) yyVals[-2 + yyTop]).getValue()).length() - 1;
    /* We may need to subtract addition offset off of first */
    /* string fragment (we optimistically take one off in*/
    /* ParserSupport.literal_concat).  Check token length*/
    /* and subtract as neeeded.*/
    if ((((Node) yyVals[-1 + yyTop]) instanceof DStrNode) && extraLength > 0) {
        Node strNode = ((DStrNode) ((Node) yyVals[-1 + yyTop])).get(0);
    }
    return yyVal;
}

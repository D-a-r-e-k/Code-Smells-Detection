public static Object case409_line1557(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    ISourcePosition position = ((Token) yyVals[-2 + yyTop]).getPosition();
    if (((Node) yyVals[-1 + yyTop]) == null) {
        yyVal = new XStrNode(position, null);
    } else if (((Node) yyVals[-1 + yyTop]) instanceof StrNode) {
        yyVal = new XStrNode(position, (ByteList) ((StrNode) yyVals[-1 + yyTop]).getValue().clone());
    } else if (((Node) yyVals[-1 + yyTop]) instanceof DStrNode) {
        yyVal = new DXStrNode(position, ((DStrNode) yyVals[-1 + yyTop]));
        ((Node) yyVal).setPosition(position);
    } else {
        yyVal = new DXStrNode(position).add(((Node) yyVals[-1 + yyTop]));
    }
    return yyVal;
}

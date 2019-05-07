public static Object case414_line1598(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = ((ListNode) yyVals[-2 + yyTop]).add(((Node) yyVals[-1 + yyTop]) instanceof EvStrNode ? new DStrNode(((ListNode) yyVals[-2 + yyTop]).getPosition()).add(((Node) yyVals[-1 + yyTop])) : ((Node) yyVals[-1 + yyTop]));
    return yyVal;
}

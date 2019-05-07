public static Object case344_line1299(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    RestArgNode rest = new UnnamedRestArgNode(((ListNode) yyVals[-1 + yyTop]).getPosition(), support.getCurrentScope().addVariable("*"));
    yyVal = support.new_args(((ListNode) yyVals[-1 + yyTop]).getPosition(), ((ListNode) yyVals[-1 + yyTop]), null, rest, null, null);
    return yyVal;
}

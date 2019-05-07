public static Object case241_line939(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = support.newArrayNode(((ListNode) yyVals[-1 + yyTop]).getPosition(), new Hash19Node(lexer.getPosition(), ((ListNode) yyVals[-1 + yyTop])));
    yyVal = support.arg_blk_pass((Node) yyVal, ((BlockPassNode) yyVals[0 + yyTop]));
    return yyVal;
}

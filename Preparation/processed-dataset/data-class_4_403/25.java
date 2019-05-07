public static Object case242_line943(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = support.arg_append(((Node) yyVals[-3 + yyTop]), new Hash19Node(lexer.getPosition(), ((ListNode) yyVals[-1 + yyTop])));
    yyVal = support.arg_blk_pass((Node) yyVal, ((BlockPassNode) yyVals[0 + yyTop]));
    return yyVal;
}

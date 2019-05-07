public static Object case373_line1403(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* Workaround for JRUBY-2326 (MRI does not enter this production for some reason)*/
    if (((Node) yyVals[-1 + yyTop]) instanceof YieldNode) {
        throw new SyntaxException(PID.BLOCK_GIVEN_TO_YIELD, ((Node) yyVals[-1 + yyTop]).getPosition(), lexer.getCurrentLine(), "block given to yield");
    }
    if (((BlockAcceptingNode) yyVals[-1 + yyTop]).getIterNode() instanceof BlockPassNode) {
        throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, ((Node) yyVals[-1 + yyTop]).getPosition(), lexer.getCurrentLine(), "Both block arg and actual block given.");
    }
    yyVal = ((BlockAcceptingNode) yyVals[-1 + yyTop]).setIterNode(((IterNode) yyVals[0 + yyTop]));
    ((Node) yyVal).setPosition(((Node) yyVals[-1 + yyTop]).getPosition());
    return yyVal;
}

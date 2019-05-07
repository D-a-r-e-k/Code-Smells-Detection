public static Object case283_line1093(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (((Node) yyVals[-1 + yyTop]) != null && ((BlockAcceptingNode) yyVals[-1 + yyTop]).getIterNode() instanceof BlockPassNode) {
        throw new SyntaxException(PID.BLOCK_ARG_AND_BLOCK_GIVEN, ((Node) yyVals[-1 + yyTop]).getPosition(), lexer.getCurrentLine(), "Both block arg and actual block given.");
    }
    yyVal = ((BlockAcceptingNode) yyVals[-1 + yyTop]).setIterNode(((IterNode) yyVals[0 + yyTop]));
    ((Node) yyVal).setPosition(((Node) yyVals[-1 + yyTop]).getPosition());
    return yyVal;
}

public static Object case2_line275(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* ENEBO: Removed !compile_for_eval which probably is to reduce warnings*/
    if (((Node) yyVals[0 + yyTop]) != null) {
        /* last expression should not be void */
        if (((Node) yyVals[0 + yyTop]) instanceof BlockNode) {
            support.checkUselessStatement(((BlockNode) yyVals[0 + yyTop]).getLast());
        } else {
            support.checkUselessStatement(((Node) yyVals[0 + yyTop]));
        }
    }
    support.getResult().setAST(support.addRootNode(((Node) yyVals[0 + yyTop]), support.getPosition(((Node) yyVals[0 + yyTop]))));
    return yyVal;
}

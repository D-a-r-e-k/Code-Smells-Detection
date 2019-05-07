public static Object case503_line1933(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    String identifier = (String) ((Token) yyVals[0 + yyTop]).getValue();
    if (!support.is_local_id(((Token) yyVals[0 + yyTop]))) {
        support.yyerror("block argument must be local variable");
    }
    support.shadowing_lvar(((Token) yyVals[0 + yyTop]));
    yyVal = new BlockArgNode(((Token) yyVals[-1 + yyTop]).getPosition(), support.arg_var(((Token) yyVals[0 + yyTop])), identifier);
    return yyVal;
}

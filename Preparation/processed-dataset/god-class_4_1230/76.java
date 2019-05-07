public static Object case499_line1918(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (!support.is_local_id(((Token) yyVals[0 + yyTop]))) {
        support.yyerror("duplicate rest argument name");
    }
    support.shadowing_lvar(((Token) yyVals[0 + yyTop]));
    yyVal = new RestArgNode(((Token) yyVals[-1 + yyTop]).getPosition(), (String) ((Token) yyVals[0 + yyTop]).getValue(), support.arg_var(((Token) yyVals[0 + yyTop])));
    return yyVal;
}

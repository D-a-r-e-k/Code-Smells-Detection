public static Object case486_line1842(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* FIXME: Resolve what the hell is going on*/
    /*                    if (support.is_local_id($1)) {
                        support.yyerror("formal argument must be local variable");
                        }*/
    support.shadowing_lvar(((Token) yyVals[0 + yyTop]));
    yyVal = ((Token) yyVals[0 + yyTop]);
    return yyVal;
}

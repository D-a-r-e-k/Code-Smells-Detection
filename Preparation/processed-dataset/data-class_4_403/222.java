public static Object case88_line616(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* if (!($$ = assignable($1, 0))) $$ = NEW_BEGIN(0);*/
    yyVal = support.assignable(((Token) yyVals[0 + yyTop]), NilImplicitNode.NIL);
    return yyVal;
}

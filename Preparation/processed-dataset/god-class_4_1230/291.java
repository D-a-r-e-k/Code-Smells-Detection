public static Object case492_line1892(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (!support.is_local_id(((Token) yyVals[-2 + yyTop]))) {
        support.yyerror("formal argument must be local variable");
    }
    support.shadowing_lvar(((Token) yyVals[-2 + yyTop]));
    support.arg_var(((Token) yyVals[-2 + yyTop]));
    yyVal = new OptArgNode(((Token) yyVals[-2 + yyTop]).getPosition(), support.assignable(((Token) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop])));
    return yyVal;
}

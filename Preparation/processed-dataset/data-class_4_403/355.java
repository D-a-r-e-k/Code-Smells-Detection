public static Object case487_line1852(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    support.arg_var(((Token) yyVals[0 + yyTop]));
    yyVal = new ArgumentNode(((ISourcePositionHolder) yyVals[0 + yyTop]).getPosition(), (String) ((Token) yyVals[0 + yyTop]).getValue());
    /*
                    $$ = new ArgAuxiliaryNode($1.getPosition(), (String) $1.getValue(), 1);
  */
    return yyVal;
}

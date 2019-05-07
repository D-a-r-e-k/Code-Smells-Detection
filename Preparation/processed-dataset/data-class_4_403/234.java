public static Object case20_line364(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (support.isInDef() || support.isInSingle()) {
        support.yyerror("BEGIN in method");
    }
    support.pushLocalScope();
    return yyVal;
}

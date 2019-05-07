public static Object case298_line1140(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (support.isInDef() || support.isInSingle()) {
        support.yyerror("class definition in method body");
    }
    support.pushLocalScope();
    return yyVal;
}

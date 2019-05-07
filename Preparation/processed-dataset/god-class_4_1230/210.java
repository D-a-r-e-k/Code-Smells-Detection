public static Object case303_line1164(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (support.isInDef() || support.isInSingle()) {
        support.yyerror("module definition in method body");
    }
    support.pushLocalScope();
    return yyVal;
}

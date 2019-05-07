public static Object case94_line641(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (support.isInDef() || support.isInSingle()) {
        support.yyerror("dynamic constant assignment");
    }
    ISourcePosition position = ((Token) yyVals[-1 + yyTop]).getPosition();
    yyVal = new ConstDeclNode(position, null, support.new_colon3(position, (String) ((Token) yyVals[0 + yyTop]).getValue()), NilImplicitNode.NIL);
    return yyVal;
}

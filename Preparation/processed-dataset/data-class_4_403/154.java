public static Object case309_line1192(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* TODO: We should use implicit nil for body, but problem (punt til later)*/
    Node body = ((Node) yyVals[-1 + yyTop]);
    /*$8 == null ? NilImplicitNode.NIL : $8;*/
    yyVal = new DefsNode(((Token) yyVals[-8 + yyTop]).getPosition(), ((Node) yyVals[-7 + yyTop]), new ArgumentNode(((Token) yyVals[-4 + yyTop]).getPosition(), (String) ((Token) yyVals[-4 + yyTop]).getValue()), ((ArgsNode) yyVals[-2 + yyTop]), support.getCurrentScope(), body);
    support.popCurrentScope();
    support.setInSingle(support.getInSingle() - 1);
    return yyVal;
}

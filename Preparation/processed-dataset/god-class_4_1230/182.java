public static Object case306_line1178(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* TODO: We should use implicit nil for body, but problem (punt til later)*/
    Node body = ((Node) yyVals[-1 + yyTop]);
    /*$5 == null ? NilImplicitNode.NIL : $5;*/
    yyVal = new DefnNode(((Token) yyVals[-5 + yyTop]).getPosition(), new ArgumentNode(((Token) yyVals[-4 + yyTop]).getPosition(), (String) ((Token) yyVals[-4 + yyTop]).getValue()), ((ArgsNode) yyVals[-2 + yyTop]), support.getCurrentScope(), body);
    support.popCurrentScope();
    support.setInDef(false);
    return yyVal;
}

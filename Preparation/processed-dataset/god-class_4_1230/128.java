public static Object case85_line594(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (support.isInDef() || support.isInSingle()) {
        support.yyerror("dynamic constant assignment");
    }
    ISourcePosition position = support.getPosition(((Node) yyVals[-2 + yyTop]));
    yyVal = new ConstDeclNode(position, null, support.new_colon2(position, ((Node) yyVals[-2 + yyTop]), (String) ((Token) yyVals[0 + yyTop]).getValue()), NilImplicitNode.NIL);
    return yyVal;
}

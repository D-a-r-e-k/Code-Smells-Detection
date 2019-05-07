public static Object case184_line720(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = support.node_assign(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
    /* FIXME: Consider fixing node_assign itself rather than single case*/
    ((Node) yyVal).setPosition(support.getPosition(((Node) yyVals[-2 + yyTop])));
    return yyVal;
}

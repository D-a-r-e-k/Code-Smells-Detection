public static Object case33_line429(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    ((AssignableNode) yyVals[-2 + yyTop]).setValueNode(((Node) yyVals[0 + yyTop]));
    yyVal = ((MultipleAsgn19Node) yyVals[-2 + yyTop]);
    ((MultipleAsgn19Node) yyVals[-2 + yyTop]).setPosition(support.getPosition(((MultipleAsgn19Node) yyVals[-2 + yyTop])));
    return yyVal;
}

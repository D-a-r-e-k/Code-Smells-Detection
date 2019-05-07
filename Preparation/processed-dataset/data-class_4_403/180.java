public static Object case255_line1007(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    Node node = null;
    if (((Node) yyVals[0 + yyTop]) instanceof ArrayNode && (node = support.splat_array(((Node) yyVals[-3 + yyTop]))) != null) {
        yyVal = support.list_concat(node, ((Node) yyVals[0 + yyTop]));
    } else {
        yyVal = support.arg_concat(((Node) yyVals[-3 + yyTop]).getPosition(), ((Node) yyVals[-3 + yyTop]), ((Node) yyVals[0 + yyTop]));
    }
    return yyVal;
}

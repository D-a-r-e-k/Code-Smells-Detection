public static Object case252_line977(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    Node node = support.splat_array(((Node) yyVals[-2 + yyTop]));
    if (node != null) {
        yyVal = support.list_append(node, ((Node) yyVals[0 + yyTop]));
    } else {
        yyVal = support.arg_append(((Node) yyVals[-2 + yyTop]), ((Node) yyVals[0 + yyTop]));
    }
    return yyVal;
}

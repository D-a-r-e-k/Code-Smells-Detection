public static Object case253_line986(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    Node node = null;
    /* FIXME: lose syntactical elements here (and others like this)*/
    if (((Node) yyVals[0 + yyTop]) instanceof ArrayNode && (node = support.splat_array(((Node) yyVals[-3 + yyTop]))) != null) {
        yyVal = support.list_concat(node, ((Node) yyVals[0 + yyTop]));
    } else {
        yyVal = support.arg_concat(support.getPosition(((Node) yyVals[-3 + yyTop])), ((Node) yyVals[-3 + yyTop]), ((Node) yyVals[0 + yyTop]));
    }
    return yyVal;
}

public static Object case26_line406(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    /* FIXME: arg_concat logic missing for opt_call_args*/
    yyVal = support.new_opElementAsgnNode(support.getPosition(((Node) yyVals[-5 + yyTop])), ((Node) yyVals[-5 + yyTop]), (String) ((Token) yyVals[-1 + yyTop]).getValue(), ((Node) yyVals[-3 + yyTop]), ((Node) yyVals[0 + yyTop]));
    return yyVal;
}

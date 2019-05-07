public static Object case384_line1446(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    if (((Node) yyVals[-3 + yyTop]) instanceof SelfNode) {
        yyVal = support.new_fcall(new Token("[]", support.getPosition(((Node) yyVals[-3 + yyTop]))), ((Node) yyVals[-1 + yyTop]), null);
    } else {
        yyVal = support.new_call(((Node) yyVals[-3 + yyTop]), new Token("[]", support.getPosition(((Node) yyVals[-3 + yyTop]))), ((Node) yyVals[-1 + yyTop]), null);
    }
    return yyVal;
}

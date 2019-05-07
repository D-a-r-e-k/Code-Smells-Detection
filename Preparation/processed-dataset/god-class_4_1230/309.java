public static Object case404_line1518(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    yyVal = ((Node) yyVals[0 + yyTop]) instanceof EvStrNode ? new DStrNode(((Node) yyVals[0 + yyTop]).getPosition()).add(((Node) yyVals[0 + yyTop])) : ((Node) yyVals[0 + yyTop]);
    /*
                    NODE *node = $1;
                    if (!node) {
                        node = NEW_STR(STR_NEW0());
                    } else {
                        node = evstr2dstr(node);
                    }
                    $$ = node;
                    */
    return yyVal;
}

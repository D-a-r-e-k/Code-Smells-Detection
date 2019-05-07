public static Object case439_line1678(ParserSupport support, RubyYaccLexer lexer, Object yyVal, Object[] yyVals, int yyTop) {
    lexer.setState(LexState.EXPR_END);
    /* DStrNode: :"some text #{some expression}"*/
    /* StrNode: :"some text"*/
    /* EvStrNode :"#{some expression}"*/
    if (((Node) yyVals[-1 + yyTop]) == null) {
        support.yyerror("empty symbol literal");
    }
    /* FIXME: No node here seems to be an empty string
                        instead of an error
                        if (!($$ = $2)) {
                        $$ = NEW_LIT(ID2SYM(rb_intern("")));
                        }
                     */
    if (((Node) yyVals[-1 + yyTop]) instanceof DStrNode) {
        yyVal = new DSymbolNode(((Token) yyVals[-2 + yyTop]).getPosition(), ((DStrNode) yyVals[-1 + yyTop]));
    } else {
        yyVal = new DSymbolNode(((Token) yyVals[-2 + yyTop]).getPosition());
        ((DSymbolNode) yyVal).add(((Node) yyVals[-1 + yyTop]));
    }
    return yyVal;
}

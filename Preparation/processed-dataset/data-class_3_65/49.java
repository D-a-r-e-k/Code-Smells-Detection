public final void mML_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = ML_COMMENT;
    int _saveIndex;
    match("/*");
    {
        _loop236: do {
            if ((LA(1) == '\r') && (LA(2) == '\n') && ((LA(3) >= '' && LA(3) <= '翾')) && ((LA(4) >= '' && LA(4) <= '翾'))) {
                match('\r');
                match('\n');
                if (inputState.guessing == 0) {
                    newline();
                }
            } else if (((LA(1) == '*') && ((LA(2) >= '' && LA(2) <= '翾')) && ((LA(3) >= '' && LA(3) <= '翾'))) && (LA(2) != '/')) {
                match('*');
            } else if ((LA(1) == '\r') && ((LA(2) >= '' && LA(2) <= '翾')) && ((LA(3) >= '' && LA(3) <= '翾')) && (true)) {
                match('\r');
                if (inputState.guessing == 0) {
                    newline();
                }
            } else if ((LA(1) == '\n')) {
                match('\n');
                if (inputState.guessing == 0) {
                    newline();
                }
            } else if ((_tokenSet_1.member(LA(1)))) {
                {
                    match(_tokenSet_1);
                }
            } else {
                break _loop236;
            }
        } while (true);
    }
    match("*/");
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

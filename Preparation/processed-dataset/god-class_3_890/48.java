public final void mSL_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = SL_COMMENT;
    int _saveIndex;
    match("//");
    {
        _loop230: do {
            if ((_tokenSet_0.member(LA(1)))) {
                {
                    match(_tokenSet_0);
                }
            } else {
                break _loop230;
            }
        } while (true);
    }
    {
        switch(LA(1)) {
            case '\n':
                {
                    match('\n');
                    break;
                }
            case '\r':
                {
                    match('\r');
                    {
                        if ((LA(1) == '\n')) {
                            match('\n');
                        } else {
                        }
                    }
                    break;
                }
            default:
                {
                }
        }
    }
    if (inputState.guessing == 0) {
        newline();
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

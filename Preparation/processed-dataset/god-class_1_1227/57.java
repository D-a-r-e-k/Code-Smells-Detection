public final void mStringLiteral(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = StringLiteral;
    int _saveIndex;
    match('"');
    {
        _loop546: do {
            if ((LA(1) == '\\') && (_tokenSet_3.member(LA(2)))) {
                mEscape(false);
            } else if ((LA(1) == '\\') && (LA(2) == '\n' || LA(2) == '\r')) {
                {
                    if ((LA(1) == '\\') && (LA(2) == '\r') && (LA(3) == '\n')) {
                        match("\\\r\n");
                    } else if ((LA(1) == '\\') && (LA(2) == '\r') && (_tokenSet_2.member(LA(3)))) {
                        match("\\\r");
                    } else if ((LA(1) == '\\') && (LA(2) == '\n')) {
                        match("\\\n");
                    } else {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                    }
                }
                if (inputState.guessing == 0) {
                    newline();
                }
            } else if ((_tokenSet_6.member(LA(1)))) {
                {
                    match(_tokenSet_6);
                }
            } else {
                break _loop546;
            }
        } while (true);
    }
    match('"');
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

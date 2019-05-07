public final void mSTRING_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = STRING_LITERAL;
    int _saveIndex;
    match('"');
    {
        _loop243: do {
            if ((LA(1) == '\\')) {
                mESC(false);
            } else if ((_tokenSet_3.member(LA(1)))) {
                {
                    match(_tokenSet_3);
                }
            } else {
                break _loop243;
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

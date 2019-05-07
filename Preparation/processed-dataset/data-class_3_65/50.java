public final void mCHAR_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = CHAR_LITERAL;
    int _saveIndex;
    match('\'');
    {
        if ((LA(1) == '\\')) {
            mESC(false);
        } else if ((_tokenSet_2.member(LA(1)))) {
            {
                match(_tokenSet_2);
            }
        } else {
            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
        }
    }
    match('\'');
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

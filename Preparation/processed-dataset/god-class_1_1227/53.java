public final void mDIRECTIVE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = DIRECTIVE;
    int _saveIndex;
    Token ld = null;
    match('#');
    mLineDirective(true);
    ld = _returnToken;
    if (inputState.guessing == 0) {
        _ttype = Token.SKIP;
        newline();
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

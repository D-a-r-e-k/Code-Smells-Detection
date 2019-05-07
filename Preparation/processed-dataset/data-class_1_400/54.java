protected final void mLineDirective(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = LineDirective;
    int _saveIndex;
    {
        _loop538: do {
            if ((_tokenSet_2.member(LA(1)))) {
                {
                    match(_tokenSet_2);
                }
            } else {
                break _loop538;
            }
        } while (true);
    }
    mEndOfLine(false);
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

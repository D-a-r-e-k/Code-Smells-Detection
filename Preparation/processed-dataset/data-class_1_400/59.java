protected final void mDecimal(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = Decimal;
    int _saveIndex;
    {
        int _cnt561 = 0;
        _loop561: do {
            if (((LA(1) >= '0' && LA(1) <= '9'))) {
                matchRange('0', '9');
            } else {
                if (_cnt561 >= 1) {
                    break _loop561;
                } else {
                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                }
            }
            _cnt561++;
        } while (true);
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

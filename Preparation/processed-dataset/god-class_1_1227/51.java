protected final void mEndOfLine(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = EndOfLine;
    int _saveIndex;
    {
        if ((LA(1) == '\r') && (LA(2) == '\n') && (true)) {
            match("\r\n");
        } else if ((LA(1) == '\r') && (true) && (true)) {
            match('\r');
        } else if ((LA(1) == '\n')) {
            match('\n');
        } else {
            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
        }
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

public final void mCPPComment(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = CPPComment;
    int _saveIndex;
    match("//");
    {
        _loop533: do {
            if ((_tokenSet_2.member(LA(1)))) {
                {
                    match(_tokenSet_2);
                }
            } else {
                break _loop533;
            }
        } while (true);
    }
    mEndOfLine(false);
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

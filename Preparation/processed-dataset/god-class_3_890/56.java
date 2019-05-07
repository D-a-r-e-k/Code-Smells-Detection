protected final void mEXPONENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = EXPONENT;
    int _saveIndex;
    {
        switch(LA(1)) {
            case 'e':
                {
                    match('e');
                    break;
                }
            case 'E':
                {
                    match('E');
                    break;
                }
            default:
                {
                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                }
        }
    }
    {
        switch(LA(1)) {
            case '+':
                {
                    match('+');
                    break;
                }
            case '-':
                {
                    match('-');
                    break;
                }
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                {
                    break;
                }
            default:
                {
                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                }
        }
    }
    {
        int _cnt292 = 0;
        _loop292: do {
            if (((LA(1) >= '0' && LA(1) <= '9'))) {
                matchRange('0', '9');
            } else {
                if (_cnt292 >= 1) {
                    break _loop292;
                } else {
                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                }
            }
            _cnt292++;
        } while (true);
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

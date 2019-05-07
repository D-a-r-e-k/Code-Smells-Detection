protected final void mESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = ESC;
    int _saveIndex;
    match('\\');
    {
        switch(LA(1)) {
            case 'n':
                {
                    match('n');
                    break;
                }
            case 'r':
                {
                    match('r');
                    break;
                }
            case 't':
                {
                    match('t');
                    break;
                }
            case 'b':
                {
                    match('b');
                    break;
                }
            case 'f':
                {
                    match('f');
                    break;
                }
            case '"':
                {
                    match('"');
                    break;
                }
            case '\'':
                {
                    match('\'');
                    break;
                }
            case '\\':
                {
                    match('\\');
                    break;
                }
            case 'u':
                {
                    {
                        int _cnt247 = 0;
                        _loop247: do {
                            if ((LA(1) == 'u')) {
                                match('u');
                            } else {
                                if (_cnt247 >= 1) {
                                    break _loop247;
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                            _cnt247++;
                        } while (true);
                    }
                    mHEX_DIGIT(false);
                    mHEX_DIGIT(false);
                    mHEX_DIGIT(false);
                    mHEX_DIGIT(false);
                    break;
                }
            case '0':
            case '1':
            case '2':
            case '3':
                {
                    matchRange('0', '3');
                    {
                        if (((LA(1) >= '0' && LA(1) <= '7')) && (_tokenSet_0.member(LA(2))) && (true) && (true)) {
                            matchRange('0', '7');
                            {
                                if (((LA(1) >= '0' && LA(1) <= '7')) && (_tokenSet_0.member(LA(2))) && (true) && (true)) {
                                    matchRange('0', '7');
                                } else if ((_tokenSet_0.member(LA(1))) && (true) && (true) && (true)) {
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                        } else if ((_tokenSet_0.member(LA(1))) && (true) && (true) && (true)) {
                        } else {
                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                        }
                    }
                    break;
                }
            case '4':
            case '5':
            case '6':
            case '7':
                {
                    matchRange('4', '7');
                    {
                        if (((LA(1) >= '0' && LA(1) <= '7')) && (_tokenSet_0.member(LA(2))) && (true) && (true)) {
                            matchRange('0', '7');
                        } else if ((_tokenSet_0.member(LA(1))) && (true) && (true) && (true)) {
                        } else {
                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                        }
                    }
                    break;
                }
            default:
                {
                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                }
        }
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

protected final void mEscape(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = Escape;
    int _saveIndex;
    match('\\');
    {
        switch(LA(1)) {
            case 'a':
                {
                    match('a');
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
            case 'v':
                {
                    match('v');
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
            case '?':
                {
                    match('?');
                    break;
                }
            case '0':
            case '1':
            case '2':
            case '3':
                {
                    {
                        matchRange('0', '3');
                    }
                    {
                        if (((LA(1) >= '0' && LA(1) <= '9')) && (_tokenSet_2.member(LA(2))) && (true)) {
                            mDigit(false);
                            {
                                if (((LA(1) >= '0' && LA(1) <= '9')) && (_tokenSet_2.member(LA(2))) && (true)) {
                                    mDigit(false);
                                } else if ((_tokenSet_2.member(LA(1))) && (true) && (true)) {
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                        } else if ((_tokenSet_2.member(LA(1))) && (true) && (true)) {
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
                    {
                        matchRange('4', '7');
                    }
                    {
                        if (((LA(1) >= '0' && LA(1) <= '9')) && (_tokenSet_2.member(LA(2))) && (true)) {
                            mDigit(false);
                        } else if ((_tokenSet_2.member(LA(1))) && (true) && (true)) {
                        } else {
                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                        }
                    }
                    break;
                }
            case 'x':
                {
                    match('x');
                    {
                        int _cnt557 = 0;
                        _loop557: do {
                            if (((LA(1) >= '0' && LA(1) <= '9')) && (_tokenSet_2.member(LA(2))) && (true)) {
                                mDigit(false);
                            } else if (((LA(1) >= 'a' && LA(1) <= 'f')) && (_tokenSet_2.member(LA(2))) && (true)) {
                                matchRange('a', 'f');
                            } else if (((LA(1) >= 'A' && LA(1) <= 'F')) && (_tokenSet_2.member(LA(2))) && (true)) {
                                matchRange('A', 'F');
                            } else {
                                if (_cnt557 >= 1) {
                                    break _loop557;
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                            _cnt557++;
                        } while (true);
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

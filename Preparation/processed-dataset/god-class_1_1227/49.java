public final void mWhitespace(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = Whitespace;
    int _saveIndex;
    {
        switch(LA(1)) {
            case '\t':
            case '':
            case ' ':
                {
                    {
                        switch(LA(1)) {
                            case ' ':
                                {
                                    match(' ');
                                    break;
                                }
                            case '\t':
                                {
                                    match('\t');
                                    break;
                                }
                            case '':
                                {
                                    match('\f');
                                    break;
                                }
                            default:
                                {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                        }
                    }
                    break;
                }
            case '\n':
            case '\r':
                {
                    {
                        if ((LA(1) == '\r') && (LA(2) == '\n')) {
                            match("\r\n");
                        } else if ((LA(1) == '\r') && (true)) {
                            match('\r');
                        } else if ((LA(1) == '\n')) {
                            match('\n');
                        } else {
                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                        }
                    }
                    if (inputState.guessing == 0) {
                        newline();
                    }
                    break;
                }
            case '\\':
                {
                    {
                        if ((LA(1) == '\\') && (LA(2) == '\r') && (LA(3) == '\n')) {
                            match("\\\r\n");
                        } else if ((LA(1) == '\\') && (LA(2) == '\r') && (true)) {
                            match("\\\r");
                        } else if ((LA(1) == '\\') && (LA(2) == '\n')) {
                            match("\\\n");
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
    if (inputState.guessing == 0) {
        _ttype = Token.SKIP;
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

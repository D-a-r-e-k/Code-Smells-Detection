public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = WS;
    int _saveIndex;
    {
        int _cnt226 = 0;
        _loop226: do {
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
                case '\n':
                case '\r':
                    {
                        {
                            if ((LA(1) == '\r') && (LA(2) == '\n') && (true) && (true)) {
                                match("\r\n");
                            } else if ((LA(1) == '\r') && (true) && (true) && (true)) {
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
                default:
                    {
                        if (_cnt226 >= 1) {
                            break _loop226;
                        } else {
                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                        }
                    }
            }
            _cnt226++;
        } while (true);
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

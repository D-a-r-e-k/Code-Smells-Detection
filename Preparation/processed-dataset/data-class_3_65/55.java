public final void mNUM_INT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = NUM_INT;
    int _saveIndex;
    boolean isDecimal = false;
    switch(LA(1)) {
        case '.':
            {
                match('.');
                if (inputState.guessing == 0) {
                    _ttype = DOT;
                }
                {
                    if (((LA(1) >= '0' && LA(1) <= '9'))) {
                        {
                            int _cnt260 = 0;
                            _loop260: do {
                                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                                    matchRange('0', '9');
                                } else {
                                    if (_cnt260 >= 1) {
                                        break _loop260;
                                    } else {
                                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                    }
                                }
                                _cnt260++;
                            } while (true);
                        }
                        {
                            if ((LA(1) == 'E' || LA(1) == 'e')) {
                                mEXPONENT(false);
                            } else {
                            }
                        }
                        {
                            if ((LA(1) == 'D' || LA(1) == 'F' || LA(1) == 'd' || LA(1) == 'f')) {
                                mFLOAT_SUFFIX(false);
                            } else {
                            }
                        }
                        if (inputState.guessing == 0) {
                            _ttype = NUM_FLOAT;
                        }
                    } else {
                    }
                }
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
                {
                    switch(LA(1)) {
                        case '0':
                            {
                                match('0');
                                if (inputState.guessing == 0) {
                                    isDecimal = true;
                                }
                                {
                                    if ((LA(1) == 'X' || LA(1) == 'x')) {
                                        {
                                            switch(LA(1)) {
                                                case 'x':
                                                    {
                                                        match('x');
                                                        break;
                                                    }
                                                case 'X':
                                                    {
                                                        match('X');
                                                        break;
                                                    }
                                                default:
                                                    {
                                                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                                    }
                                            }
                                        }
                                        {
                                            int _cnt267 = 0;
                                            _loop267: do {
                                                if ((_tokenSet_4.member(LA(1))) && (true) && (true) && (true)) {
                                                    mHEX_DIGIT(false);
                                                } else {
                                                    if (_cnt267 >= 1) {
                                                        break _loop267;
                                                    } else {
                                                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                                    }
                                                }
                                                _cnt267++;
                                            } while (true);
                                        }
                                    } else {
                                        boolean synPredMatched272 = false;
                                        if ((((LA(1) >= '0' && LA(1) <= '9')) && (true) && (true) && (true))) {
                                            int _m272 = mark();
                                            synPredMatched272 = true;
                                            inputState.guessing++;
                                            try {
                                                {
                                                    {
                                                        int _cnt270 = 0;
                                                        _loop270: do {
                                                            if (((LA(1) >= '0' && LA(1) <= '9'))) {
                                                                matchRange('0', '9');
                                                            } else {
                                                                if (_cnt270 >= 1) {
                                                                    break _loop270;
                                                                } else {
                                                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                                                }
                                                            }
                                                            _cnt270++;
                                                        } while (true);
                                                    }
                                                    {
                                                        switch(LA(1)) {
                                                            case '.':
                                                                {
                                                                    match('.');
                                                                    break;
                                                                }
                                                            case 'E':
                                                            case 'e':
                                                                {
                                                                    mEXPONENT(false);
                                                                    break;
                                                                }
                                                            case 'D':
                                                            case 'F':
                                                            case 'd':
                                                            case 'f':
                                                                {
                                                                    mFLOAT_SUFFIX(false);
                                                                    break;
                                                                }
                                                            default:
                                                                {
                                                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                                                }
                                                        }
                                                    }
                                                }
                                            } catch (RecognitionException pe) {
                                                synPredMatched272 = false;
                                            }
                                            rewind(_m272);
                                            inputState.guessing--;
                                        }
                                        if (synPredMatched272) {
                                            {
                                                int _cnt274 = 0;
                                                _loop274: do {
                                                    if (((LA(1) >= '0' && LA(1) <= '9'))) {
                                                        matchRange('0', '9');
                                                    } else {
                                                        if (_cnt274 >= 1) {
                                                            break _loop274;
                                                        } else {
                                                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                                        }
                                                    }
                                                    _cnt274++;
                                                } while (true);
                                            }
                                        } else if (((LA(1) >= '0' && LA(1) <= '7')) && (true) && (true) && (true)) {
                                            {
                                                int _cnt276 = 0;
                                                _loop276: do {
                                                    if (((LA(1) >= '0' && LA(1) <= '7'))) {
                                                        matchRange('0', '7');
                                                    } else {
                                                        if (_cnt276 >= 1) {
                                                            break _loop276;
                                                        } else {
                                                            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                                        }
                                                    }
                                                    _cnt276++;
                                                } while (true);
                                            }
                                        } else {
                                        }
                                    }
                                }
                                break;
                            }
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
                                {
                                    matchRange('1', '9');
                                }
                                {
                                    _loop279: do {
                                        if (((LA(1) >= '0' && LA(1) <= '9'))) {
                                            matchRange('0', '9');
                                        } else {
                                            break _loop279;
                                        }
                                    } while (true);
                                }
                                if (inputState.guessing == 0) {
                                    isDecimal = true;
                                }
                                break;
                            }
                        default:
                            {
                                throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                            }
                    }
                }
                {
                    if ((LA(1) == 'L' || LA(1) == 'l')) {
                        {
                            switch(LA(1)) {
                                case 'l':
                                    {
                                        match('l');
                                        break;
                                    }
                                case 'L':
                                    {
                                        match('L');
                                        break;
                                    }
                                default:
                                    {
                                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                    }
                            }
                        }
                    } else if (((LA(1) == '.' || LA(1) == 'D' || LA(1) == 'E' || LA(1) == 'F' || LA(1) == 'd' || LA(1) == 'e' || LA(1) == 'f')) && (isDecimal)) {
                        {
                            switch(LA(1)) {
                                case '.':
                                    {
                                        match('.');
                                        {
                                            _loop284: do {
                                                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                                                    matchRange('0', '9');
                                                } else {
                                                    break _loop284;
                                                }
                                            } while (true);
                                        }
                                        {
                                            if ((LA(1) == 'E' || LA(1) == 'e')) {
                                                mEXPONENT(false);
                                            } else {
                                            }
                                        }
                                        {
                                            if ((LA(1) == 'D' || LA(1) == 'F' || LA(1) == 'd' || LA(1) == 'f')) {
                                                mFLOAT_SUFFIX(false);
                                            } else {
                                            }
                                        }
                                        break;
                                    }
                                case 'E':
                                case 'e':
                                    {
                                        mEXPONENT(false);
                                        {
                                            if ((LA(1) == 'D' || LA(1) == 'F' || LA(1) == 'd' || LA(1) == 'f')) {
                                                mFLOAT_SUFFIX(false);
                                            } else {
                                            }
                                        }
                                        break;
                                    }
                                case 'D':
                                case 'F':
                                case 'd':
                                case 'f':
                                    {
                                        mFLOAT_SUFFIX(false);
                                        break;
                                    }
                                default:
                                    {
                                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                    }
                            }
                        }
                        if (inputState.guessing == 0) {
                            _ttype = NUM_FLOAT;
                        }
                    } else {
                    }
                }
                break;
            }
        default:
            {
                throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
            }
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
        _token = makeToken(_ttype);
        _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
}

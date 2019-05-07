public final void mNumber(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = Number;
    int _saveIndex;
    boolean synPredMatched576 = false;
    if ((((LA(1) >= '0' && LA(1) <= '9')) && (_tokenSet_7.member(LA(2))) && (true))) {
        int _m576 = mark();
        synPredMatched576 = true;
        inputState.guessing++;
        try {
            {
                {
                    int _cnt574 = 0;
                    _loop574: do {
                        if (((LA(1) >= '0' && LA(1) <= '9'))) {
                            mDigit(false);
                        } else {
                            if (_cnt574 >= 1) {
                                break _loop574;
                            } else {
                                throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                            }
                        }
                        _cnt574++;
                    } while (true);
                }
                {
                    switch(LA(1)) {
                        case '.':
                            {
                                match('.');
                                break;
                            }
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
            }
        } catch (RecognitionException pe) {
            synPredMatched576 = false;
        }
        rewind(_m576);
        inputState.guessing--;
    }
    if (synPredMatched576) {
        {
            int _cnt578 = 0;
            _loop578: do {
                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                    mDigit(false);
                } else {
                    if (_cnt578 >= 1) {
                        break _loop578;
                    } else {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                    }
                }
                _cnt578++;
            } while (true);
        }
        {
            switch(LA(1)) {
                case '.':
                    {
                        match('.');
                        {
                            _loop581: do {
                                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                                    mDigit(false);
                                } else {
                                    break _loop581;
                                }
                            } while (true);
                        }
                        {
                            if ((LA(1) == 'E' || LA(1) == 'e')) {
                                mExponent(false);
                            } else {
                            }
                        }
                        if (inputState.guessing == 0) {
                            _ttype = FLOATONE;
                        }
                        break;
                    }
                case 'E':
                case 'e':
                    {
                        mExponent(false);
                        if (inputState.guessing == 0) {
                            _ttype = FLOATTWO;
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
            switch(LA(1)) {
                case 'F':
                case 'f':
                    {
                        mFloatSuffix(false);
                        break;
                    }
                case 'L':
                case 'l':
                    {
                        mLongSuffix(false);
                        break;
                    }
                default:
                    {
                    }
            }
        }
    } else {
        boolean synPredMatched585 = false;
        if (((LA(1) == '.') && (LA(2) == '.'))) {
            int _m585 = mark();
            synPredMatched585 = true;
            inputState.guessing++;
            try {
                {
                    match("...");
                }
            } catch (RecognitionException pe) {
                synPredMatched585 = false;
            }
            rewind(_m585);
            inputState.guessing--;
        }
        if (synPredMatched585) {
            match("...");
            if (inputState.guessing == 0) {
                _ttype = ELLIPSIS;
            }
        } else if ((LA(1) == '0') && (LA(2) == 'X' || LA(2) == 'x')) {
            match('0');
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
                int _cnt601 = 0;
                _loop601: do {
                    switch(LA(1)) {
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            {
                                matchRange('a', 'f');
                                break;
                            }
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            {
                                matchRange('A', 'F');
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
                                mDigit(false);
                                break;
                            }
                        default:
                            {
                                if (_cnt601 >= 1) {
                                    break _loop601;
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                    }
                    _cnt601++;
                } while (true);
            }
            {
                _loop603: do {
                    switch(LA(1)) {
                        case 'L':
                        case 'l':
                            {
                                mLongSuffix(false);
                                break;
                            }
                        case 'U':
                        case 'u':
                            {
                                mUnsignedSuffix(false);
                                break;
                            }
                        default:
                            {
                                break _loop603;
                            }
                    }
                } while (true);
            }
            if (inputState.guessing == 0) {
                _ttype = HEXADECIMALINT;
            }
        } else if ((LA(1) == '.') && (true)) {
            match('.');
            if (inputState.guessing == 0) {
                _ttype = DOT;
            }
            {
                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                    {
                        int _cnt588 = 0;
                        _loop588: do {
                            if (((LA(1) >= '0' && LA(1) <= '9'))) {
                                mDigit(false);
                            } else {
                                if (_cnt588 >= 1) {
                                    break _loop588;
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                            _cnt588++;
                        } while (true);
                    }
                    {
                        if ((LA(1) == 'E' || LA(1) == 'e')) {
                            mExponent(false);
                        } else {
                        }
                    }
                    if (inputState.guessing == 0) {
                        _ttype = FLOATONE;
                    }
                    {
                        switch(LA(1)) {
                            case 'F':
                            case 'f':
                                {
                                    mFloatSuffix(false);
                                    break;
                                }
                            case 'L':
                            case 'l':
                                {
                                    mLongSuffix(false);
                                    break;
                                }
                            default:
                                {
                                }
                        }
                    }
                } else {
                }
            }
        } else if ((LA(1) == '0') && (true) && (true)) {
            match('0');
            {
                _loop592: do {
                    if (((LA(1) >= '0' && LA(1) <= '7'))) {
                        matchRange('0', '7');
                    } else {
                        break _loop592;
                    }
                } while (true);
            }
            {
                _loop594: do {
                    switch(LA(1)) {
                        case 'L':
                        case 'l':
                            {
                                mLongSuffix(false);
                                break;
                            }
                        case 'U':
                        case 'u':
                            {
                                mUnsignedSuffix(false);
                                break;
                            }
                        default:
                            {
                                break _loop594;
                            }
                    }
                } while (true);
            }
            if (inputState.guessing == 0) {
                _ttype = OCTALINT;
            }
        } else if (((LA(1) >= '1' && LA(1) <= '9')) && (true) && (true)) {
            matchRange('1', '9');
            {
                _loop596: do {
                    if (((LA(1) >= '0' && LA(1) <= '9'))) {
                        mDigit(false);
                    } else {
                        break _loop596;
                    }
                } while (true);
            }
            {
                _loop598: do {
                    switch(LA(1)) {
                        case 'L':
                        case 'l':
                            {
                                mLongSuffix(false);
                                break;
                            }
                        case 'U':
                        case 'u':
                            {
                                mUnsignedSuffix(false);
                                break;
                            }
                        default:
                            {
                                break _loop598;
                            }
                    }
                } while (true);
            }
            if (inputState.guessing == 0) {
                _ttype = DECIMALINT;
            }
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

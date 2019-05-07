public Token nextToken() throws TokenStreamException {
    Token theRetToken = null;
    tryAgain: for (; ; ) {
        Token _token = null;
        int _ttype = Token.INVALID_TYPE;
        resetText();
        try {
            // for char stream error handling 
            try {
                // for lexical error handling 
                switch(LA(1)) {
                    case '?':
                        {
                            mQUESTION(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '(':
                        {
                            mLPAREN(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case ')':
                        {
                            mRPAREN(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '[':
                        {
                            mLBRACK(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case ']':
                        {
                            mRBRACK(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '{':
                        {
                            mLCURLY(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '}':
                        {
                            mRCURLY(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case ':':
                        {
                            mCOLON(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case ',':
                        {
                            mCOMMA(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '~':
                        {
                            mBNOT(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case ';':
                        {
                            mSEMI(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '\t':
                    case '\n':
                    case '':
                    case '\r':
                    case ' ':
                        {
                            mWS(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '\'':
                        {
                            mCHAR_LITERAL(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '"':
                        {
                            mSTRING_LITERAL(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '$':
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                    case 'G':
                    case 'H':
                    case 'I':
                    case 'J':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'S':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    case 'Z':
                    case '_':
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'n':
                    case 'o':
                    case 'p':
                    case 'q':
                    case 'r':
                    case 's':
                    case 't':
                    case 'u':
                    case 'v':
                    case 'w':
                    case 'x':
                    case 'y':
                    case 'z':
                        {
                            mIDENT(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '.':
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
                            mNUM_INT(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    default:
                        if ((LA(1) == '>') && (LA(2) == '>') && (LA(3) == '>') && (LA(4) == '=')) {
                            mBSR_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '>') && (LA(2) == '>') && (LA(3) == '=')) {
                            mSR_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '>') && (LA(2) == '>') && (LA(3) == '>') && (true)) {
                            mBSR(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '<') && (LA(2) == '<') && (LA(3) == '=')) {
                            mSL_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '=') && (LA(2) == '=')) {
                            mEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '!') && (LA(2) == '=')) {
                            mNOT_EQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '/') && (LA(2) == '=')) {
                            mDIV_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '+') && (LA(2) == '=')) {
                            mPLUS_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '+') && (LA(2) == '+')) {
                            mINC(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '-') && (LA(2) == '=')) {
                            mMINUS_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '-') && (LA(2) == '-')) {
                            mDEC(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '*') && (LA(2) == '=')) {
                            mSTAR_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '%') && (LA(2) == '=')) {
                            mMOD_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '>') && (LA(2) == '>') && (true)) {
                            mSR(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '>') && (LA(2) == '=')) {
                            mGE(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '<') && (LA(2) == '<') && (true)) {
                            mSL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '<') && (LA(2) == '=')) {
                            mLE(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '^') && (LA(2) == '=')) {
                            mBXOR_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '|') && (LA(2) == '=')) {
                            mBOR_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '|') && (LA(2) == '|')) {
                            mLOR(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '&') && (LA(2) == '=')) {
                            mBAND_ASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '&') && (LA(2) == '&')) {
                            mLAND(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '/') && (LA(2) == '/')) {
                            mSL_COMMENT(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '/') && (LA(2) == '*')) {
                            mML_COMMENT(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '=') && (true)) {
                            mASSIGN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '!') && (true)) {
                            mLNOT(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '/') && (true)) {
                            mDIV(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '+') && (true)) {
                            mPLUS(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '-') && (true)) {
                            mMINUS(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '*') && (true)) {
                            mSTAR(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '%') && (true)) {
                            mMOD(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '>') && (true)) {
                            mGT(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '<') && (true)) {
                            mLT(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '^') && (true)) {
                            mBXOR(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '|') && (true)) {
                            mBOR(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '&') && (true)) {
                            mBAND(true);
                            theRetToken = _returnToken;
                        } else {
                            if (LA(1) == EOF_CHAR) {
                                uponEOF();
                                _returnToken = makeToken(Token.EOF_TYPE);
                            } else {
                                throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
                            }
                        }
                }
                if (_returnToken == null)
                    continue tryAgain;
                // found SKIP token 
                _ttype = _returnToken.getType();
                _returnToken.setType(_ttype);
                return _returnToken;
            } catch (RecognitionException e) {
                throw new TokenStreamRecognitionException(e);
            }
        } catch (CharStreamException cse) {
            if (cse instanceof CharStreamIOException) {
                throw new TokenStreamIOException(((CharStreamIOException) cse).io);
            } else {
                throw new TokenStreamException(cse.getMessage());
            }
        }
    }
}

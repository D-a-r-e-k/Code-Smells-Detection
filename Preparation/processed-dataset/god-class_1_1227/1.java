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
                    case ',':
                        {
                            mCOMMA(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '?':
                        {
                            mQUESTIONMARK(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case ';':
                        {
                            mSEMICOLON(true);
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
                            mLSQUARE(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case ']':
                        {
                            mRSQUARE(true);
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
                    case '~':
                        {
                            mTILDE(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '\t':
                    case '\n':
                    case '':
                    case '\r':
                    case ' ':
                    case '\\':
                        {
                            mWhitespace(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '#':
                        {
                            mDIRECTIVE(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '\'':
                        {
                            mCharLiteral(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    case '"':
                        {
                            mStringLiteral(true);
                            theRetToken = _returnToken;
                            break;
                        }
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
                            mID(true);
                            theRetToken = _returnToken;
                            break;
                        }
                    default:
                        if ((LA(1) == '>') && (LA(2) == '>') && (LA(3) == '=')) {
                            mSHIFTRIGHTEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '<') && (LA(2) == '<') && (LA(3) == '=')) {
                            mSHIFTLEFTEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '-') && (LA(2) == '>') && (LA(3) == '*')) {
                            mPOINTERTOMBR(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '-') && (LA(2) == '>') && (true)) {
                            mPOINTERTO(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '=') && (LA(2) == '=')) {
                            mEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '!') && (LA(2) == '=')) {
                            mNOTEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '<') && (LA(2) == '=')) {
                            mLESSTHANOREQUALTO(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '>') && (LA(2) == '=')) {
                            mGREATERTHANOREQUALTO(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '/') && (LA(2) == '=')) {
                            mDIVIDEEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '+') && (LA(2) == '=')) {
                            mPLUSEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '+') && (LA(2) == '+')) {
                            mPLUSPLUS(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '-') && (LA(2) == '=')) {
                            mMINUSEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '-') && (LA(2) == '-')) {
                            mMINUSMINUS(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '*') && (LA(2) == '=')) {
                            mTIMESEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '%') && (LA(2) == '=')) {
                            mMODEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '>') && (LA(2) == '>') && (true)) {
                            mSHIFTRIGHT(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '<') && (LA(2) == '<') && (true)) {
                            mSHIFTLEFT(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '&') && (LA(2) == '&')) {
                            mAND(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '|') && (LA(2) == '|')) {
                            mOR(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '&') && (LA(2) == '=')) {
                            mBITWISEANDEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '|') && (LA(2) == '=')) {
                            mBITWISEOREQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '^') && (LA(2) == '=')) {
                            mBITWISEXOREQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '.') && (LA(2) == '*')) {
                            mDOTMBR(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == ':') && (LA(2) == ':')) {
                            mSCOPE(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '/') && (LA(2) == '*')) {
                            mComment(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '/') && (LA(2) == '/')) {
                            mCPPComment(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '=') && (true)) {
                            mASSIGNEQUAL(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == ':') && (true)) {
                            mCOLON(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '<') && (true)) {
                            mLESSTHAN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '>') && (true)) {
                            mGREATERTHAN(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '/') && (true)) {
                            mDIVIDE(true);
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
                        } else if ((LA(1) == '!') && (true)) {
                            mNOT(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '&') && (true)) {
                            mAMPERSAND(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '|') && (true)) {
                            mBITWISEOR(true);
                            theRetToken = _returnToken;
                        } else if ((LA(1) == '^') && (true)) {
                            mBITWISEXOR(true);
                            theRetToken = _returnToken;
                        } else if ((_tokenSet_0.member(LA(1))) && (true)) {
                            mNumber(true);
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
                _ttype = testLiteralsTable(_ttype);
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

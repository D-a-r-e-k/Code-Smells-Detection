/**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
public Token yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;
    // cached fields: 
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char[] zzBufferL = zzBuffer;
    char[] zzCMapL = ZZ_CMAP;
    int[] zzTransL = ZZ_TRANS;
    int[] zzRowMapL = ZZ_ROWMAP;
    int[] zzAttrL = ZZ_ATTRIBUTE;
    while (true) {
        zzMarkedPosL = zzMarkedPos;
        boolean zzR = false;
        for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL; zzCurrentPosL++) {
            switch(zzBufferL[zzCurrentPosL]) {
                case '':
                case '':
                case '':
                case ' ':
                case ' ':
                    yyline++;
                    yycolumn = 0;
                    zzR = false;
                    break;
                case '\r':
                    yyline++;
                    yycolumn = 0;
                    zzR = true;
                    break;
                case '\n':
                    if (zzR)
                        zzR = false;
                    else {
                        yyline++;
                        yycolumn = 0;
                    }
                    break;
                default:
                    zzR = false;
                    yycolumn++;
            }
        }
        if (zzR) {
            // peek one character ahead if it is \n (if we have counted one line too much) 
            boolean zzPeek;
            if (zzMarkedPosL < zzEndReadL)
                zzPeek = zzBufferL[zzMarkedPosL] == '\n';
            else if (zzAtEOF)
                zzPeek = false;
            else {
                boolean eof = zzRefill();
                zzEndReadL = zzEndRead;
                zzMarkedPosL = zzMarkedPos;
                zzBufferL = zzBuffer;
                if (eof)
                    zzPeek = false;
                else
                    zzPeek = zzBufferL[zzMarkedPosL] == '\n';
            }
            if (zzPeek)
                yyline--;
        }
        if (zzMarkedPosL > zzStartRead) {
            switch(zzBufferL[zzMarkedPosL - 1]) {
                case '\n':
                case '':
                case '':
                case '':
                case ' ':
                case ' ':
                    zzAtBOL = true;
                    break;
                case '\r':
                    if (zzMarkedPosL < zzEndReadL)
                        zzAtBOL = zzBufferL[zzMarkedPosL] != '\n';
                    else if (zzAtEOF)
                        zzAtBOL = false;
                    else {
                        boolean eof = zzRefill();
                        zzMarkedPosL = zzMarkedPos;
                        zzEndReadL = zzEndRead;
                        zzBufferL = zzBuffer;
                        if (eof)
                            zzAtBOL = false;
                        else
                            zzAtBOL = zzBufferL[zzMarkedPosL] != '\n';
                    }
                    break;
                default:
                    zzAtBOL = false;
            }
        }
        zzAction = -1;
        zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
        if (zzAtBOL)
            zzState = ZZ_LEXSTATE[zzLexicalState + 1];
        else
            zzState = ZZ_LEXSTATE[zzLexicalState];
        zzForAction: {
            while (true) {
                if (zzCurrentPosL < zzEndReadL)
                    zzInput = zzBufferL[zzCurrentPosL++];
                else if (zzAtEOF) {
                    zzInput = YYEOF;
                    break zzForAction;
                } else {
                    // store back cached positions 
                    zzCurrentPos = zzCurrentPosL;
                    zzMarkedPos = zzMarkedPosL;
                    boolean eof = zzRefill();
                    // get translated positions and possibly new buffer 
                    zzCurrentPosL = zzCurrentPos;
                    zzMarkedPosL = zzMarkedPos;
                    zzBufferL = zzBuffer;
                    zzEndReadL = zzEndRead;
                    if (eof) {
                        zzInput = YYEOF;
                        break zzForAction;
                    } else {
                        zzInput = zzBufferL[zzCurrentPosL++];
                    }
                }
                int zzNext = zzTransL[zzRowMapL[zzState] + zzCMapL[zzInput]];
                if (zzNext == -1)
                    break zzForAction;
                zzState = zzNext;
                int zzAttributes = zzAttrL[zzState];
                if ((zzAttributes & 1) == 1) {
                    zzAction = zzState;
                    zzMarkedPosL = zzCurrentPosL;
                    if ((zzAttributes & 8) == 8)
                        break zzForAction;
                }
            }
        }
        // store back cached position 
        zzMarkedPos = zzMarkedPosL;
        switch(zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
            case 19:
                {
                    commandBuffer.append(yytext());
                    debug("SQL '", yytext());
                    yybegin(SQL);
                }
            case 35:
                break;
            case 9:
                {
                    commandBuffer.setLength(0);
                    yybegin(SPECIAL);
                }
            case 36:
                break;
            case 30:
                {
                    pushbackTrim();
                    /* embedded comment may disable opening quotes and closing ; */
                    debug("Spl. -- Comment", yytext());
                }
            case 37:
                break;
            case 10:
                {
                    commandBuffer.setLength(0);
                    yybegin(EDIT);
                }
            case 38:
                break;
            case 21:
                {
                    yybegin(YYINITIAL);
                    debug("Gobbled", yytext());
                    prompt();
                }
            case 39:
                break;
            case 31:
                {
                    /* Ignore top-level traditional comments */
                    debug("/**/ Comment", yytext());
                }
            case 40:
                break;
            case 8:
                {
                    return new Token(Token.SQL_TYPE, yyline);
                }
            case 41:
                break;
            case 2:
                {
                    prompt();
                }
            case 42:
                break;
            case 22:
                {
                    if (commandBuffer.toString().trim().equals(".")) {
                        commandBuffer.setLength(0);
                        yybegin(RAW);
                        rawLeadinPrompt();
                        if (rawPrompt != null)
                            prompt(rawPrompt);
                    } else {
                        requestedState = YYINITIAL;
                        yybegin(PROMPT_CHANGE_STATE);
                        pushbackTrim();
                        return new Token(Token.SPECIAL_TYPE, commandBuffer, yyline);
                    }
                }
            case 43:
                break;
            case 28:
                {
                    specialAppendState = false;
                    commandBuffer.append(yytext());
                    /* embedded comment may disable opening quotes and closing ; */
                    debug("SQL -- Comment", yytext());
                }
            case 44:
                break;
            case 17:
                {
                    if (commandBuffer.length() > 0)
                        commandBuffer.append('\n');
                    commandBuffer.append(strippedYytext());
                    if (rawPrompt != null)
                        prompt(rawPrompt);
                }
            case 45:
                break;
            case 26:
                {
                    yybegin(requestedState);
                    prompt();
                }
            case 46:
                break;
            case 4:
                {
                    commandBuffer.setLength(0);
                    yybegin(MACRO);
                }
            case 47:
                break;
            case 6:
                {
                    /* Ignore top-level whte space */
                    debug("Whitespace", yytext());
                }
            case 48:
                break;
            case 18:
                {
                    commandBuffer.append(yytext());
                }
            case 49:
                break;
            case 11:
                {
                    specialAppendState = false;
                    commandBuffer.append(yytext());
                }
            case 50:
                break;
            case 25:
                {
                    requestedState = YYINITIAL;
                    yybegin(PROMPT_CHANGE_STATE);
                    pushbackTrim();
                    return new Token(Token.MACRO_TYPE, commandBuffer, yyline);
                }
            case 51:
                break;
            case 16:
                {
                    if (interactive && !specialAppendState) {
                        requestedState = YYINITIAL;
                        yybegin(PROMPT_CHANGE_STATE);
                        pushbackTrim();
                        trimBuffer();
                        return new Token(Token.BUFFER_TYPE, commandBuffer, yyline);
                    }
                    specialAppendState = false;
                    commandBuffer.append(yytext());
                }
            case 52:
                break;
            case 29:
                {
                    yybegin(YYINITIAL);
                    prompt();
                    return new Token(Token.RAWEXEC_TYPE, commandBuffer, yyline);
                }
            case 53:
                break;
            case 27:
                {
                    yybegin(YYINITIAL);
                    prompt();
                    return new Token(Token.RAW_TYPE, commandBuffer, yyline);
                }
            case 54:
                break;
            case 14:
                {
                    specialAppendState = false;
                    yybegin(YYINITIAL);
                    return new Token(Token.SQL_TYPE, commandBuffer, yyline);
                }
            case 55:
                break;
            case 33:
                {
                    /* embedded comment may disable opening closing \n */
                    debug("Spl. /**/ Comment", yytext());
                }
            case 56:
                break;
            case 3:
                {
                    yybegin(GOBBLE);
                    return new Token(Token.SYNTAX_ERR_TYPE, yytext(), yyline);
                }
            case 57:
                break;
            case 20:
                {
                    commandBuffer.append(yytext());
                    yybegin(SQL);
                    debug("SQL \"", yytext());
                }
            case 58:
                break;
            case 1:
                {
                    setCommandBuffer(yytext());
                    yybegin(SQL);
                }
            case 59:
                break;
            case 23:
                {
                    requestedState = YYINITIAL;
                    yybegin(PROMPT_CHANGE_STATE);
                    pushbackTrim();
                    return new Token(Token.PL_TYPE, commandBuffer, yyline);
                }
            case 60:
                break;
            case 12:
                {
                    specialAppendState = false;
                    commandBuffer.append(yytext());
                    if (sqlPrompt != null)
                        prompt(sqlPrompt);
                }
            case 61:
                break;
            case 24:
                {
                    requestedState = YYINITIAL;
                    yybegin(PROMPT_CHANGE_STATE);
                    pushbackTrim();
                    return new Token(Token.EDIT_TYPE, commandBuffer, yyline);
                }
            case 62:
                break;
            case 7:
                {
                    debug("-- Comment", yytext());
                }
            case 63:
                break;
            case 15:
                {
                    specialAppendState = false;
                    commandBuffer.append(yytext());
                    yybegin(SQL_SINGLE_QUOTED);
                }
            case 64:
                break;
            case 5:
                {
                    commandBuffer.setLength(0);
                    yybegin(PL);
                }
            case 65:
                break;
            case 34:
                {
                    /* These are commands which may contain nested commands and/or which
     * require the closing semicolon to sent to the DB engine.
     * The BEGIN and DECLARE needed for PL/SQL probably do not need to
     * terminate the line, as we have it specified here, but I'd rather not be
     * too liberal with proprietary SQL like this, because it's easy to
     * envision other proprietary or non-proprietary commands beginning with
     * DECLARE or BEGIN. */
                    setCommandBuffer(strippedYytext());
                    yybegin(RAW);
                    rawLeadinPrompt();
                    if (rawPrompt != null)
                        prompt(rawPrompt);
                }
            case 66:
                break;
            case 32:
                {
                    specialAppendState = false;
                    commandBuffer.append(yytext());
                    /* embedded comment may disable opening quotes and closing ; */
                    debug("SQL /**/ Comment", yytext());
                }
            case 67:
                break;
            case 13:
                {
                    specialAppendState = false;
                    commandBuffer.append(yytext());
                    yybegin(SQL_DOUBLE_QUOTED);
                }
            case 68:
                break;
            default:
                if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
                    zzAtEOF = true;
                    zzDoEOF();
                    switch(zzLexicalState) {
                        case SPECIAL:
                            {
                                yybegin(YYINITIAL);
                                return new Token(Token.UNTERM_TYPE, commandBuffer, yyline);
                            }
                        case 115:
                            break;
                        case SQL_DOUBLE_QUOTED:
                            {
                                yybegin(YYINITIAL);
                                return new Token(Token.UNTERM_TYPE, commandBuffer, yyline);
                            }
                        case 116:
                            break;
                        case SQL_SINGLE_QUOTED:
                            {
                                yybegin(YYINITIAL);
                                return new Token(Token.UNTERM_TYPE, commandBuffer, yyline);
                            }
                        case 117:
                            break;
                        case SQL:
                            {
                                yybegin(YYINITIAL);
                                return new Token(Token.UNTERM_TYPE, commandBuffer, yyline);
                            }
                        case 118:
                            break;
                        case EDIT:
                            {
                                yybegin(YYINITIAL);
                                return new Token(Token.UNTERM_TYPE, commandBuffer, yyline);
                            }
                        case 119:
                            break;
                        case PL:
                            {
                                yybegin(YYINITIAL);
                                return new Token(Token.UNTERM_TYPE, commandBuffer, yyline);
                            }
                        case 120:
                            break;
                        case MACRO:
                            {
                                yybegin(YYINITIAL);
                                return new Token(Token.UNTERM_TYPE, commandBuffer, yyline);
                            }
                        case 121:
                            break;
                        default:
                            return null;
                    }
                } else {
                    zzScanError(ZZ_NO_MATCH);
                }
        }
    }
}

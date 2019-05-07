void AddChar(char c) {
    onlyChar++;
    matchSingleChar = c;
    int i;
    char temp;
    char temp1;
    if ((int) c < 128) // ASCII char 
    {
        AddASCIIMove(c);
        return;
    }
    if (charMoves == null)
        charMoves = new char[10];
    int len = charMoves.length;
    if (charMoves[len - 1] != 0) {
        charMoves = ExpandCharArr(charMoves, 10);
        len += 10;
    }
    for (i = 0; i < len; i++) if (charMoves[i] == 0 || charMoves[i] > c)
        break;
    if (!unicodeWarningGiven && c > 0xff && !Options.getJavaUnicodeEscape() && !Options.getUserCharStream()) {
        unicodeWarningGiven = true;
        JavaCCErrors.warning(LexGen.curRE, "Non-ASCII characters used in regular expression.\n" + "Please make sure you use the correct Reader when you create the parser, " + "one that can handle your character set.");
    }
    temp = charMoves[i];
    charMoves[i] = c;
    for (i++; i < len; i++) {
        if (temp == 0)
            break;
        temp1 = charMoves[i];
        charMoves[i] = temp;
        temp = temp1;
    }
}

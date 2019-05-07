void AddRange(char left, char right) {
    onlyChar = 2;
    int i;
    char tempLeft1, tempLeft2, tempRight1, tempRight2;
    if (left < 128) {
        if (right < 128) {
            for (; left <= right; left++) AddASCIIMove(left);
            return;
        }
        for (; left < 128; left++) AddASCIIMove(left);
    }
    if (!unicodeWarningGiven && (left > 0xff || right > 0xff) && !Options.getJavaUnicodeEscape() && !Options.getUserCharStream()) {
        unicodeWarningGiven = true;
        JavaCCErrors.warning(LexGen.curRE, "Non-ASCII characters used in regular expression.\n" + "Please make sure you use the correct Reader when you create the parser, " + "one that can handle your character set.");
    }
    if (rangeMoves == null)
        rangeMoves = new char[20];
    int len = rangeMoves.length;
    if (rangeMoves[len - 1] != 0) {
        rangeMoves = ExpandCharArr(rangeMoves, 20);
        len += 20;
    }
    for (i = 0; i < len; i += 2) if (rangeMoves[i] == 0 || (rangeMoves[i] > left) || ((rangeMoves[i] == left) && (rangeMoves[i + 1] > right)))
        break;
    tempLeft1 = rangeMoves[i];
    tempRight1 = rangeMoves[i + 1];
    rangeMoves[i] = left;
    rangeMoves[i + 1] = right;
    for (i += 2; i < len; i += 2) {
        if (tempLeft1 == 0)
            break;
        tempLeft2 = rangeMoves[i];
        tempRight2 = rangeMoves[i + 1];
        rangeMoves[i] = tempLeft1;
        rangeMoves[i + 1] = tempRight1;
        tempLeft1 = tempLeft2;
        tempRight1 = tempRight2;
    }
}

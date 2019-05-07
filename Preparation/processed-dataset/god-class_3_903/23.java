public int getFirstValidPos(String s, int i, int len) {
    if (onlyChar == 1) {
        char c = matchSingleChar;
        while (c != s.charAt(i) && ++i < len) ;
        return i;
    }
    do {
        if (CanMoveUsingChar(s.charAt(i)))
            return i;
    } while (++i < len);
    return i;
}

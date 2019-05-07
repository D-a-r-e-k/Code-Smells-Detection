static void dumpFormattedString(String str) {
    char ch = ' ';
    char prevChar;
    boolean indentOn = true;
    for (int i = 0; i < str.length(); i++) {
        prevChar = ch;
        ch = str.charAt(i);
        if (ch == '\n' && prevChar == '\r') {
        } else if (ch == '\n' || ch == '\r') {
            if (indentOn) {
                phase1NewLine();
            } else {
                ostr.println("");
            }
        } else if (ch == '') {
            indentamt += 2;
        } else if (ch == '') {
            indentamt -= 2;
        } else if (ch == '') {
            indentOn = false;
        } else if (ch == '') {
            indentOn = true;
        } else {
            ostr.print(ch);
        }
    }
}

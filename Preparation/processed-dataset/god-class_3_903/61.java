public static void DumpNonAsciiMoveMethods(java.io.PrintWriter ostr) {
    if (!Options.getJavaUnicodeEscape() && !unicodeWarningGiven)
        return;
    if (nonAsciiTableForMethod.size() <= 0)
        return;
    for (int i = 0; i < nonAsciiTableForMethod.size(); i++) {
        NfaState tmp = (NfaState) nonAsciiTableForMethod.get(i);
        tmp.DumpNonAsciiMoveMethod(ostr);
    }
}

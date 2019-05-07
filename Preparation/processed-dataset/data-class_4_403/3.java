/** computes list of expected tokens on error by tracing the tables.
      @param state for which to compute the list.
      @return list of token names.
    */
protected String[] yyExpecting(int state) {
    int token, n, len = 0;
    boolean[] ok = new boolean[yyNames.length];
    if ((n = yySindex[state]) != 0)
        for (token = n < 0 ? -n : 0; token < yyNames.length && n + token < yyTable.length; ++token) if (yyCheck[n + token] == token && !ok[token] && yyNames[token] != null) {
            ++len;
            ok[token] = true;
        }
    if ((n = yyRindex[state]) != 0)
        for (token = n < 0 ? -n : 0; token < yyNames.length && n + token < yyTable.length; ++token) if (yyCheck[n + token] == token && !ok[token] && yyNames[token] != null) {
            ++len;
            ok[token] = true;
        }
    String result[] = new String[len];
    for (n = token = 0; n < len; ++token) if (ok[token])
        result[n++] = yyNames[token];
    return result;
}

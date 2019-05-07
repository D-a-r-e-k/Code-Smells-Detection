/** index-checked interface to {@link #yyNames}.
      @param token single character or <tt>%token</tt> value.
      @return token name or <tt>[illegal]</tt> or <tt>[unknown]</tt>.
    */
public static final String yyName(int token) {
    if (token < 0 || token > yyNames.length)
        return "[illegal]";
    String name;
    if ((name = yyNames[token]) != null)
        return name;
    return "[unknown]";
}

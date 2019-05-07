/**
   * Returns the text matched by the current regular expression.
   */
public final String yytext() {
    return new String(zzBuffer, zzStartRead, zzMarkedPos - zzStartRead);
}

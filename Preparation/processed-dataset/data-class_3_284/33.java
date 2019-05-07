/**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
public final void yybegin(int newState) {
    zzLexicalState = newState;
}

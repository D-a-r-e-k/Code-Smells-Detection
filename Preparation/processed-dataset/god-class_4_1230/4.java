/** the generated parser, with debugging messages.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @param yydebug debug message writer implementing <tt>yyDebug</tt>, or <tt>null</tt>.
      @return result of the last reduction, if any.
    */
public Object yyparse(RubyYaccLexer yyLex, Object ayydebug) throws java.io.IOException {
    this.yydebug = (jay.yydebug.yyDebug) ayydebug;
    return yyparse(yyLex);
}

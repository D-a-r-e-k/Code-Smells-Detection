// line 2027 "Ruby19Parser.y" 
/** The parse method use an lexer stream and parse it to an AST node 
     * structure
     */
public RubyParserResult parse(ParserConfiguration configuration, LexerSource source) throws IOException {
    support.reset();
    support.setConfiguration(configuration);
    support.setResult(new RubyParserResult());
    lexer.reset();
    lexer.setSource(source);
    lexer.setEncoding(configuration.getKCode().getEncoding());
    Object debugger = null;
    if (configuration.isDebug()) {
        try {
            Class yyDebugAdapterClass = Class.forName("jay.yydebug.yyDebugAdapter");
            debugger = yyDebugAdapterClass.newInstance();
        } catch (IllegalAccessException iae) {
        } catch (InstantiationException ie) {
        } catch (ClassNotFoundException cnfe) {
        }
    }
    //yyparse(lexer, new jay.yydebug.yyAnim("JRuby", 9)); 
    yyparse(lexer, debugger);
    return support.getResult();
}

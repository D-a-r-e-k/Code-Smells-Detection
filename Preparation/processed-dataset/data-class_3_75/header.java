void method0() { 
/**
   * Definitions of constants that identify the kind of regular
   * expression production this is.
   */
public static final int TOKEN = 0, SKIP = 1, MORE = 2, SPECIAL = 3;
/**
   * The image of the above constants.
   */
public static final String[] kindImage = { "TOKEN", "SKIP", "MORE", "SPECIAL" };
/**
   * The starting line and column of this token production.
   */
private int column;
private int line;
/**
   * The states in which this regular expression production exists.  If
   * this array is null, then "<*>" has been specified and this regular
   * expression exists in all states.  However, this null value is
   * replaced by a String array that includes all lexical state names
   * during the semanticization phase.
   */
public String[] lexStates;
/**
   * The kind of this token production - TOKEN, SKIP, MORE, or SPECIAL.
   */
public int kind;
/**
   * The list of regular expression specifications that comprise this
   * production.  Each entry is a "RegExprSpec".
   */
public List respecs = new ArrayList();
/**
   * This is true if this corresponds to a production that actually
   * appears in the input grammar.  Otherwise (if this is created to
   * describe a regular expression that is part of the BNF) this is set
   * to false.
   */
public boolean isExplicit = true;
/**
   * This is true if case is to be ignored within the regular expressions
   * of this token production.
   */
public boolean ignoreCase = false;
/**
   * The first and last tokens from the input stream that represent this
   * production.
   */
public Token firstToken, lastToken;
}

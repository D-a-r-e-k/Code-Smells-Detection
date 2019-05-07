void method0() { 
/** This character denotes the end of file */
public static final int YYEOF = -1;
/** initial size of the lookahead buffer */
private static final int ZZ_BUFFERSIZE = 16384;
/** lexical states */
public static final int SPECIAL = 12;
public static final int SQL_DOUBLE_QUOTED = 8;
public static final int SQL_SINGLE_QUOTED = 6;
public static final int GOBBLE = 10;
public static final int RAW = 4;
public static final int SQL = 2;
public static final int YYINITIAL = 0;
public static final int EDIT = 16;
public static final int PL = 14;
public static final int PROMPT_CHANGE_STATE = 20;
public static final int MACRO = 18;
/**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
private static final int ZZ_LEXSTATE[] = { 0, 0, 1, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11 };
/** 
   * Translates characters to character classes
   */
private static final String ZZ_CMAP_PACKED = "\11\0\1\6\1\2\1\0\1\6\1\1\22\0\1\6\1\0\1\3" + "\4\0\1\33\2\0\1\5\2\0\1\7\1\32\1\4\12\0\1\31" + "\1\10\5\0\1\20\1\11\1\16\1\26\1\12\1\22\1\13\1\0" + "\1\14\2\0\1\27\1\0\1\15\1\24\1\25\1\0\1\17\1\0" + "\1\21\1\23\6\0\1\30\4\0\1\20\1\11\1\16\1\26\1\12" + "\1\22\1\13\1\0\1\14\2\0\1\27\1\0\1\15\1\24\1\25" + "\1\0\1\17\1\0\1\21\1\23ﾊ\0";
/** 
   * Translates characters to character classes
   */
private static final char[] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);
/** 
   * Translates DFA states to action switch labels.
   */
private static final int[] ZZ_ACTION = zzUnpackAction();
private static final String ZZ_ACTION_PACKED_0 = "\14\0\1\1\2\2\1\3\1\4\1\5\1\6\1\7" + "\1\10\3\1\1\11\1\12\1\13\2\14\1\15\2\13" + "\1\16\1\17\2\20\1\13\1\0\2\21\1\0\1\22" + "\1\23\1\22\1\24\2\25\1\22\2\26\2\22\2\27" + "\2\30\2\31\2\32\7\0\2\33\10\0\2\34\2\35" + "\1\0\2\36\1\37\3\0\1\40\1\41\3\0\2\42" + "\23\0";
/** 
   * Translates a state to a row index in the transition table
   */
private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
private static final String ZZ_ROWMAP_PACKED_0 = "\0\0\0\34\0\70\0\124\0\160\0\214\0\250\0\304" + "\0\340\0\374\0Ę\0Ĵ\0Ő\0Ŭ\0Ő\0Ő" + "\0ƈ\0Ő\0Ƥ\0ǀ\0Ő\0ǜ\0Ǹ\0Ȕ" + "\0Ő\0Ő\0Ő\0Ȱ\0Ő\0Ő\0Ɍ\0ɨ" + "\0Ő\0Ő\0ʄ\0Ő\0ʠ\0ʼ\0˘\0Ő" + "\0˴\0̐\0̬\0͈\0Ő\0ͤ\0Ő\0Ő" + "\0΀\0Ő\0Μ\0θ\0ϔ\0Ő\0ϰ\0Ő" + "\0Ќ\0Ő\0Ш\0Ő\0ф\0Ѡ\0Ѽ\0Ҙ" + "\0Ҵ\0Ӑ\0ʠ\0Ӭ\0Ő\0Ԉ\0Ԥ\0Հ" + "\0՜\0ո\0֔\0ְ\0׌\0ר\0Ő\0؄" + "\0Ő\0ؠ\0ؼ\0Ő\0Ő\0٘\0ٴ\0ڐ" + "\0Ő\0Ő\0ڬ\0ۈ\0ۤ\0܀\0Ő\0ܜ" + "\0ܸ\0ݔ\0ݰ\0ތ\0ި\0߄\0ߠ\0߼" + "\0࠘\0࠴\0ࡐ\0࡬\0࢈\0ࢤ\0ࣀ\0ࣜ" + "\0ࣸ\0औ";
/** 
   * The transition table of the DFA
   */
private static final int[] ZZ_TRANS = zzUnpackTrans();
private static final String ZZ_TRANS_PACKED_0 = "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24" + "\1\25\1\26\4\15\1\27\7\15\1\30\1\15\1\31" + "\1\32\1\15\1\20\1\33\1\34\1\35\1\36\1\37" + "\2\33\1\40\1\41\22\33\1\42\1\33\1\43\1\44" + "\1\36\1\37\1\33\1\45\1\40\1\41\22\33\1\42" + "\1\46\1\47\1\50\3\46\1\4\23\46\1\51\1\46" + "\33\52\1\53\3\54\1\55\30\54\1\7\1\56\1\57" + "\31\7\1\60\1\61\1\62\1\60\1\63\2\60\1\64" + "\25\60\1\65\1\66\1\60\1\63\2\60\1\64\25\60" + "\1\67\1\70\32\60\1\71\1\72\1\60\1\63\2\60" + "\1\64\24\60\1\0\1\73\1\74\67\0\1\17\36\0" + "\1\75\34\0\1\23\25\0\1\24\2\0\31\24\12\0" + "\1\76\40\0\1\77\26\0\1\100\23\0\1\35\36\0" + "\1\101\35\0\1\102\26\0\1\44\32\0\1\43\1\44" + "\3\0\1\103\25\0\1\46\1\47\1\50\31\46\2\0" + "\1\50\31\0\1\46\1\104\1\105\3\46\1\51\1\46" + "\1\106\23\46\33\52\34\0\1\60\3\54\1\0\30\54" + "\2\0\1\57\33\0\1\62\36\0\1\107\35\0\1\110" + "\26\0\1\66\33\0\1\70\33\0\1\72\33\0\1\74" + "\31\0\5\75\1\111\26\75\13\0\1\112\32\0\1\113" + "\37\0\1\114\15\0\5\101\1\115\26\101\1\102\1\116" + "\1\117\31\102\2\0\1\105\31\0\1\46\1\120\1\121" + "\3\46\1\106\25\46\5\107\1\122\26\107\1\110\1\123" + "\1\124\31\110\4\75\1\125\1\111\26\75\14\0\1\126" + "\37\0\1\127\42\0\1\130\4\0\4\101\1\131\1\115" + "\26\101\2\0\1\117\33\0\1\121\31\0\4\107\1\132" + "\1\122\26\107\2\0\1\124\46\0\1\133\37\0\1\134" + "\32\0\1\135\14\0\1\136\1\137\3\0\1\133\37\0" + "\1\140\40\0\1\141\16\0\1\137\37\0\1\142\37\0" + "\1\133\27\0\1\142\13\0\1\143\2\0\1\144\31\0" + "\1\145\27\0\1\146\31\0\1\147\42\0\1\150\25\0" + "\1\151\33\0\1\152\36\0\1\153\24\0\1\154\35\0" + "\1\155\45\0\1\156\31\0\1\157\32\0\1\160\25\0" + "\1\161\35\0\1\162\14\0\1\161\1\136\1\137\31\161" + "\12\0\1\161\21\0";
/* error codes */
private static final int ZZ_UNKNOWN_ERROR = 0;
private static final int ZZ_NO_MATCH = 1;
private static final int ZZ_PUSHBACK_2BIG = 2;
/* error messages for the codes above */
private static final String ZZ_ERROR_MSG[] = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
private static final String ZZ_ATTRIBUTE_PACKED_0 = "\14\0\1\11\1\1\2\11\1\1\1\11\2\1\1\11" + "\3\1\3\11\1\1\2\11\2\1\2\11\1\1\1\11" + "\1\1\1\0\1\1\1\11\1\0\3\1\1\11\1\1" + "\2\11\1\1\1\11\3\1\1\11\1\1\1\11\1\1" + "\1\11\1\1\1\11\7\0\1\1\1\11\10\0\1\1" + "\1\11\1\1\1\11\1\0\1\1\2\11\3\0\2\11" + "\3\0\1\1\1\11\23\0";
/** the input device */
private java.io.Reader zzReader;
/** the current state of the DFA */
private int zzState;
/** the current lexical state */
private int zzLexicalState = YYINITIAL;
/** this buffer contains the current text to be matched and is
      the source of the yytext() string */
private char zzBuffer[] = new char[ZZ_BUFFERSIZE];
/** the textposition at the last accepting state */
private int zzMarkedPos;
/** the textposition at the last state to be included in yytext */
private int zzPushbackPos;
/** the current text position in the buffer */
private int zzCurrentPos;
/** startRead marks the beginning of the yytext() string in the buffer */
private int zzStartRead;
/** endRead marks the last character in the buffer, that has been read
      from input */
private int zzEndRead;
/** number of newlines encountered up to the start of the matched text */
private int yyline;
/** the number of characters up to the start of the matched text */
private int yychar;
/**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
private int yycolumn;
/** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
private boolean zzAtBOL = true;
/** zzAtEOF == true <=> the scanner is at the EOF */
private boolean zzAtEOF;
/** denotes if the user-EOF-code has already been executed */
private boolean zzEOFDone;
/* user code: */
private static FrameworkLogger logger = FrameworkLogger.getLog(SqlFileScanner.class);
private StringBuffer commandBuffer = new StringBuffer();
private boolean interactive;
private PrintStream psStd = System.out;
private String magicPrefix;
private int requestedState = YYINITIAL;
private String rawLeadinPrompt;
private boolean specialAppendState;
//private String sqlPrompt = "+sql> "; 
private String sqlPrompt = null;
//private String sqltoolPrompt = "sql> "; 
private String sqltoolPrompt = null;
//private String rawPrompt = "raw> "; 
private String rawPrompt = null;
}

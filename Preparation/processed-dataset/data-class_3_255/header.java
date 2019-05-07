void method0() { 
/** Eliminate the arabic vowels */
public static final int AR_NOVOWEL = ArabicLigaturizer.ar_novowel;
/** Compose the tashkeel in the ligatures. */
public static final int AR_COMPOSEDTASHKEEL = ArabicLigaturizer.ar_composedtashkeel;
/** Do some extra double ligatures. */
public static final int AR_LIG = ArabicLigaturizer.ar_lig;
/**
     * Digit shaping option: Replace European digits (U+0030...U+0039) by Arabic-Indic digits.
     */
public static final int DIGITS_EN2AN = ArabicLigaturizer.DIGITS_EN2AN;
/**
     * Digit shaping option: Replace Arabic-Indic digits by European digits (U+0030...U+0039).
     */
public static final int DIGITS_AN2EN = ArabicLigaturizer.DIGITS_AN2EN;
/**
     * Digit shaping option:
     * Replace European digits (U+0030...U+0039) by Arabic-Indic digits
     * if the most recent strongly directional character
     * is an Arabic letter (its Bidi direction value is RIGHT_TO_LEFT_ARABIC).
     * The initial state at the start of the text is assumed to be not an Arabic,
     * letter, so European digits at the start of the text will not change.
     * Compare to DIGITS_ALEN2AN_INIT_AL.
     */
public static final int DIGITS_EN2AN_INIT_LR = ArabicLigaturizer.DIGITS_EN2AN_INIT_LR;
/**
     * Digit shaping option:
     * Replace European digits (U+0030...U+0039) by Arabic-Indic digits
     * if the most recent strongly directional character
     * is an Arabic letter (its Bidi direction value is RIGHT_TO_LEFT_ARABIC).
     * The initial state at the start of the text is assumed to be an Arabic,
     * letter, so European digits at the start of the text will change.
     * Compare to DIGITS_ALEN2AN_INT_LR.
     */
public static final int DIGITS_EN2AN_INIT_AL = ArabicLigaturizer.DIGITS_EN2AN_INIT_AL;
/**
     * Digit type option: Use Arabic-Indic digits (U+0660...U+0669).
     */
public static final int DIGIT_TYPE_AN = ArabicLigaturizer.DIGIT_TYPE_AN;
/**
     * Digit type option: Use Eastern (Extended) Arabic-Indic digits (U+06f0...U+06f9).
     */
public static final int DIGIT_TYPE_AN_EXTENDED = ArabicLigaturizer.DIGIT_TYPE_AN_EXTENDED;
protected int runDirection = PdfWriter.RUN_DIRECTION_DEFAULT;
/** the space char ratio */
public static final float GLOBAL_SPACE_CHAR_RATIO = 0;
/** Initial value of the status. */
public static final int START_COLUMN = 0;
/** Signals that there is no more text available. */
public static final int NO_MORE_TEXT = 1;
/** Signals that there is no more column. */
public static final int NO_MORE_COLUMN = 2;
/** The column is valid. */
protected static final int LINE_STATUS_OK = 0;
/** The line is out the column limits. */
protected static final int LINE_STATUS_OFFLIMITS = 1;
/** The line cannot fit this column position. */
protected static final int LINE_STATUS_NOLINE = 2;
/** Upper bound of the column. */
protected float maxY;
/** Lower bound of the column. */
protected float minY;
protected float leftX;
protected float rightX;
/** The column alignment. Default is left alignment. */
protected int alignment = Element.ALIGN_LEFT;
/** The left column bound. */
protected ArrayList<float[]> leftWall;
/** The right column bound. */
protected ArrayList<float[]> rightWall;
/** The chunks that form the text. */
//    protected ArrayList chunks = new ArrayList(); 
protected BidiLine bidiLine;
/** The current y line location. Text will be written at this line minus the leading. */
protected float yLine;
/**
     * The X position after the last line that has been written.
     * @since 5.0.3
     */
protected float lastX;
/** The leading for the current line. */
protected float currentLeading = 16;
/** The fixed text leading. */
protected float fixedLeading = 16;
/** The text leading that is multiplied by the biggest font size in the line. */
protected float multipliedLeading = 0;
/** The <CODE>PdfContent</CODE> where the text will be written to. */
protected PdfContentByte canvas;
protected PdfContentByte[] canvases;
/** The line status when trying to fit a line to a column. */
protected int lineStatus;
/** The first paragraph line indent. */
protected float indent = 0;
/** The following paragraph lines indent. */
protected float followingIndent = 0;
/** The right paragraph lines indent. */
protected float rightIndent = 0;
/** The extra space between paragraphs. */
protected float extraParagraphSpace = 0;
/** The width of the line when the column is defined as a simple rectangle. */
protected float rectangularWidth = -1;
protected boolean rectangularMode = false;
/** Holds value of property spaceCharRatio. */
private float spaceCharRatio = GLOBAL_SPACE_CHAR_RATIO;
private boolean lastWasNewline = true;
/** Holds value of property linesWritten. */
private int linesWritten;
private float firstLineY;
private boolean firstLineYDone = false;
/** Holds value of property arabicOptions. */
private int arabicOptions = 0;
protected float descender;
protected boolean composite = false;
protected ColumnText compositeColumn;
protected LinkedList<Element> compositeElements;
protected int listIdx = 0;
private boolean splittedRow;
protected Phrase waitPhrase;
/** if true, first line height is adjusted so that the max ascender touches the top */
private boolean useAscender = false;
/** Holds value of property filledWidth. */
private float filledWidth;
private boolean adjustFirstLine = true;
}

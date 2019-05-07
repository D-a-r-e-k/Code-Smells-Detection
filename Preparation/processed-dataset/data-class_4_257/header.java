void method0() { 
/** The <CODE>PdfWriter</CODE>. */
protected PdfWriter writer;
// LISTENER METHODS START 
//	[L0] ElementListener interface 
/** This is the PdfContentByte object, containing the text. */
protected PdfContentByte text;
/** This is the PdfContentByte object, containing the borders and other Graphics. */
protected PdfContentByte graphics;
/** This represents the leading of the lines. */
protected float leading = 0;
/** This represents the current alignment of the PDF Elements. */
protected int alignment = Element.ALIGN_LEFT;
/** This is the current height of the document. */
protected float currentHeight = 0;
/**
     * Signals that onParagraph is valid (to avoid that a Chapter/Section title is treated as a Paragraph).
     * @since 2.1.2
     */
protected boolean isSectionTitle = false;
/**
     * Signals that the current leading has to be subtracted from a YMark object when positive.
     * @since 2.1.2
     */
protected int leadingCount = 0;
/** The current active <CODE>PdfAction</CODE> when processing an <CODE>Anchor</CODE>. */
protected PdfAction anchorAction = null;
//	[L3] DocListener interface 
protected int textEmptySize;
// [C9] Metadata for the page 
/** XMP Metadata for the page. */
protected byte[] xmpMetadata = null;
//	[L5] DocListener interface 
/** margin in x direction starting from the left. Will be valid in the next page */
protected float nextMarginLeft;
/** margin in x direction starting from the right. Will be valid in the next page */
protected float nextMarginRight;
/** margin in y direction starting from the top. Will be valid in the next page */
protected float nextMarginTop;
/** margin in y direction starting from the bottom. Will be valid in the next page */
protected float nextMarginBottom;
// DOCLISTENER METHODS END 
/** Signals that OnOpenDocument should be called. */
protected boolean firstPageEvent = true;
/** The line that is currently being written. */
protected PdfLine line = null;
/** The lines that are written until now. */
protected ArrayList<PdfLine> lines = new ArrayList<PdfLine>();
/** Holds the type of the last element, that has been added to the document. */
protected int lastElementType = -1;
/** The characters to be applied the hanging punctuation. */
static final String hangingPunctuation = ".,;:'";
protected Indentation indentation = new Indentation();
//	Info Dictionary and Catalog 
/** some meta information about the Document. */
protected PdfInfo info = new PdfInfo();
//	[C1] outlines 
/** This is the root outline of the document. */
protected PdfOutline rootOutline;
/** This is the current <CODE>PdfOutline</CODE> in the hierarchy of outlines. */
protected PdfOutline currentOutline;
//  [C3] PdfViewerPreferences interface 
/** Contains the Viewer preferences of this PDF document. */
protected PdfViewerPreferencesImp viewerPreferences = new PdfViewerPreferencesImp();
//	[C4] Page labels 
protected PdfPageLabels pageLabels;
/**
     * Stores the destinations keyed by name. Value is a <Code>Destination</Code>.
     */
protected TreeMap<String, Destination> localDestinations = new TreeMap<String, Destination>();
/**
     * Stores a list of document level JavaScript actions.
     */
int jsCounter;
protected HashMap<String, PdfObject> documentLevelJS = new HashMap<String, PdfObject>();
protected static final DecimalFormat SIXTEEN_DIGITS = new DecimalFormat("0000000000000000");
protected HashMap<String, PdfObject> documentFileAttachment = new HashMap<String, PdfObject>();
//	[C6] document level actions 
protected String openActionName;
protected PdfAction openActionAction;
protected PdfDictionary additionalActions;
//	[C7] portable collections 
protected PdfCollection collection;
//	[C8] AcroForm 
PdfAnnotationsImp annotationsImp;
//	[F12] tagged PDF 
protected int markPoint;
//	[U1] page sizes 
/** This is the size of the next page. */
protected Rectangle nextPageSize = null;
/** This is the size of the several boxes of the current Page. */
protected HashMap<String, PdfRectangle> thisBoxSize = new HashMap<String, PdfRectangle>();
/** This is the size of the several boxes that will be used in
     * the next page. */
protected HashMap<String, PdfRectangle> boxSize = new HashMap<String, PdfRectangle>();
//	[U2] empty pages 
/** This checks if the page is empty. */
private boolean pageEmpty = true;
//	[U3] page actions 
/** The duration of the page */
protected int duration = -1;
// negative values will indicate no duration 
/** The page transition */
protected PdfTransition transition = null;
protected PdfDictionary pageAA = null;
//	[U8] thumbnail images 
protected PdfIndirectReference thumb;
//	[M0] Page resources contain references to fonts, extgstate, images,... 
/** This are the page resources of the current Page. */
protected PageResources pageResources;
//	[M3] Images 
/** Holds value of property strictImageSequence. */
protected boolean strictImageSequence = false;
/** This is the position where the image ends. */
protected float imageEnd = -1;
/** This is the image that could not be shown on a previous page. */
protected Image imageWait = null;
}

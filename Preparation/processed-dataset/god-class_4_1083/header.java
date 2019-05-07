void method0() { 
HashMap<PdfReader, IntHashtable> readers2intrefs = new HashMap<PdfReader, IntHashtable>();
HashMap<PdfReader, RandomAccessFileOrArray> readers2file = new HashMap<PdfReader, RandomAccessFileOrArray>();
RandomAccessFileOrArray file;
PdfReader reader;
IntHashtable myXref = new IntHashtable();
/** Integer(page number) -> PageStamp */
HashMap<PdfDictionary, PageStamp> pagesToContent = new HashMap<PdfDictionary, PageStamp>();
boolean closed = false;
/** Holds value of property rotateContents. */
private boolean rotateContents = true;
protected AcroFields acroFields;
protected boolean flat = false;
protected boolean flatFreeText = false;
protected int namePtr[] = { 0 };
protected HashSet<String> partialFlattening = new HashSet<String>();
protected boolean useVp = false;
protected PdfViewerPreferencesImp viewerPreferences = new PdfViewerPreferencesImp();
protected HashSet<PdfTemplate> fieldTemplates = new HashSet<PdfTemplate>();
protected boolean fieldsAdded = false;
protected int sigFlags = 0;
protected boolean append;
protected IntHashtable marked;
protected int initialXrefSize;
protected PdfAction openAction;
}

void method0() { 
/**
	 * The iText developers are not responsible if you decide to change the
	 * value of this static parameter.
	 * @since 5.0.2
	 */
public static boolean unethicalreading = false;
static final PdfName pageInhCandidates[] = { PdfName.MEDIABOX, PdfName.ROTATE, PdfName.RESOURCES, PdfName.CROPBOX };
static final byte endstream[] = PdfEncodings.convertToBytes("endstream", null);
static final byte endobj[] = PdfEncodings.convertToBytes("endobj", null);
protected PRTokeniser tokens;
// Each xref pair is a position 
// type 0 -> -1, 0 
// type 1 -> offset, 0 
// type 2 -> index, obj num 
protected int xref[];
protected HashMap<Integer, IntHashtable> objStmMark;
protected IntHashtable objStmToOffset;
protected boolean newXrefType;
private ArrayList<PdfObject> xrefObj;
PdfDictionary rootPages;
protected PdfDictionary trailer;
protected PdfDictionary catalog;
protected PageRefs pageRefs;
protected PRAcroForm acroForm = null;
protected boolean acroFormParsed = false;
protected boolean encrypted = false;
protected boolean rebuilt = false;
protected int freeXref;
protected boolean tampered = false;
protected int lastXref;
protected int eofPos;
protected char pdfVersion;
protected PdfEncryption decrypt;
protected byte password[] = null;
//added by ujihara for decryption 
protected Key certificateKey = null;
//added by Aiken Sam for certificate decryption 
protected Certificate certificate = null;
//added by Aiken Sam for certificate decryption 
protected String certificateKeyProvider = null;
//added by Aiken Sam for certificate decryption 
private boolean ownerPasswordUsed;
protected ArrayList<PdfString> strings = new ArrayList<PdfString>();
protected boolean sharedStreams = true;
protected boolean consolidateNamedDestinations = false;
protected boolean remoteToLocalNamedDestinations = false;
protected int rValue;
protected int pValue;
private int objNum;
private int objGen;
private int fileLength;
private boolean hybridXref;
private int lastXrefPartial = -1;
private boolean partial;
private PRIndirectReference cryptoRef;
private PdfViewerPreferencesImp viewerPreferences = new PdfViewerPreferencesImp();
private boolean encryptionError;
/**
     * Holds value of property appendable.
     */
private boolean appendable;
// Track how deeply nested the current object is, so 
// we know when to return an individual null or boolean, or 
// reuse one of the static ones. 
private int readDepth = 0;
}

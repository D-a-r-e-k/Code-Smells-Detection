void method0() { 
/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
public static final String XMLSS_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "xmlss";
private static final String XMLSS_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.xmlss.";
/**
	 *
	 */
protected static final String JR_PAGE_ANCHOR_PREFIX = "JR_PAGE_ANCHOR_";
/**
	 *
	 */
protected static final String HORIZONTAL_ALIGN_LEFT = "start";
protected static final String HORIZONTAL_ALIGN_RIGHT = "end";
protected static final String HORIZONTAL_ALIGN_CENTER = "center";
protected static final String HORIZONTAL_ALIGN_JUSTIFY = "justified";
/**
	 *
	 */
protected static final String VERTICAL_ALIGN_TOP = "top";
protected static final String VERTICAL_ALIGN_MIDDLE = "middle";
protected static final String VERTICAL_ALIGN_BOTTOM = "bottom";
public static final String IMAGE_NAME_PREFIX = "img_";
protected static final int IMAGE_NAME_PREFIX_LEGTH = IMAGE_NAME_PREFIX.length();
protected static final String[] PAGE_LAYOUT = new String[] { "Portrait", "Portrait", "Landscape" };
/**
	 *
	 */
protected Writer tempBodyWriter = null;
protected Writer tempStyleWriter = null;
protected JRExportProgressMonitor progressMonitor = null;
protected Map rendererToImagePathMap = null;
protected Map imageMaps;
protected List imagesToProcess = null;
protected int reportIndex = 0;
protected int pageIndex = 0;
protected int tableIndex = 0;
protected boolean startPage;
/**
	 *
	 */
protected String encoding = null;
/**
	 *
	 */
protected boolean isWrapBreakWord = false;
/**
	 * @deprecated
	 */
protected Map fontMap = null;
private LinkedList backcolorStack;
private Color backcolor;
private XmlssStyleCache styleCache = null;
protected ExporterNature nature = null;
protected File destFile = null;
/**
	 *
	 */
protected boolean isOnePagePerSheet;
protected boolean isRemoveEmptySpaceBetweenRows;
protected boolean isRemoveEmptySpaceBetweenColumns;
protected boolean isWhitePageBackground;
protected boolean isAutoDetectCellType = false;
protected boolean isDetectCellType;
protected boolean isFontSizeFixEnabled;
protected boolean isIgnoreGraphics;
protected boolean isCollapseRowSpan;
protected boolean isIgnoreCellBorder;
protected boolean isIgnorePageMargins;
protected int maxRowsPerSheet;
protected String[] sheetNames = null;
/**
	 * used for counting the total number of sheets
	 */
protected int sheetIndex = 0;
/**
	 * used when indexing the identical sheet generated names with ordering numbers;
	 * contains sheet names as keys and the number of occurences of each sheet name as values
	 */
protected Map sheetNamesMap = null;
protected String currentSheetName = null;
/**
	 *
	 */
protected JRFont defaultFont = null;
protected Map formatPatternsMap = null;
protected OrientationEnum pageOrientation;
}

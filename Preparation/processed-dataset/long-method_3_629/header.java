void method0() { 
/** Logger for the BrowseController. **/
private static Log log = LogFactory.getLog(BrowseController.class);
/** The BrowseHelper handles replacement of URLs in the resources. **/
private BrowseHelper browseHelper = null;
/** The QualityReviewFacade for this controller. **/
private QualityReviewFacade qualityReviewFacade = null;
private final int MAX_MEMORY_SIZE = 1024 * 1024;
//private final int MAX_MEMORY_SIZE = 0;  
/** The buffer size for reading from the file. */
private final int BYTE_BUFFER_SIZE = 1024 * 8;
private static Pattern p = Pattern.compile("\\/(\\d+)\\/(.*)");
private static Pattern CHARSET_PATTERN = Pattern.compile(";\\s+charset=([A-Za-z0-9].[A-Za-z0-9_\\-\\.:]*)");
private static Charset CHARSET_LATIN_1 = Charset.forName("ISO-8859-1");
}

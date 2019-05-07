void method0() { 
private static Logger logger = Logger.getLogger("org.archive.crawler.settings.XMLSettingsHandler");
private Locator locator;
private CrawlerSettings settings;
private SettingsHandler settingsHandler;
private Map<String, ElementHandler> handlers = new HashMap<String, ElementHandler>();
private Stack<ElementHandler> handlerStack = new Stack<ElementHandler>();
private Stack<Object> stack = new Stack<Object>();
/** Keeps track of elements which subelements should be skipped. */
private Stack<Boolean> skip = new Stack<Boolean>();
private StringBuffer buffer = new StringBuffer();
private String value;
}

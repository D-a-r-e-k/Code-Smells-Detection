void method0() { 
public static final String NAME = "vfs.browser";
//{{{ Browser types  
/**
	 * Open file dialog mode. Equals JFileChooser.OPEN_DIALOG for
	 * backwards compatibility.
	 */
public static final int OPEN_DIALOG = 0;
/**
	 * Save file dialog mode. Equals JFileChooser.SAVE_DIALOG for
	 * backwards compatibility.
	 */
public static final int SAVE_DIALOG = 1;
/**
	 * Choose directory dialog mode.
	 */
public static final int BROWSER_DIALOG = 4;
/**
	 * Choose directory dialog mode.
	 */
public static final int CHOOSE_DIRECTORY_DIALOG = 3;
/**
	 * Stand-alone browser mode.
	 */
public static final int BROWSER = 2;
//}}}  
//{{{ filesActivated() method  
// canDoubleClickClose set to false when ENTER pressed  
public static final int M_OPEN = 0;
public static final int M_OPEN_NEW_VIEW = 1;
public static final int M_OPEN_NEW_PLAIN_VIEW = 2;
public static final int M_OPEN_NEW_SPLIT = 3;
public static final int M_INSERT = 4;
//}}}  
//{{{ Package-private members  
String currentEncoding;
boolean autoDetectEncoding;
//}}}  
//}}}  
//{{{ Private members  
private static final ActionContext actionContext;
//{{{ Instance variables  
private EventListenerList listenerList;
private View view;
private boolean horizontalLayout;
private String path;
private JPanel pathAndFilterPanel;
private HistoryTextField pathField;
private JComponent defaultFocusComponent;
private JCheckBox filterCheckbox;
private HistoryComboBoxEditor filterEditor;
private JComboBox filterField;
private Box toolbarBox;
private Box topBox;
private FavoritesMenuButton favorites;
private PluginsMenuButton plugins;
private BrowserView browserView;
private int mode;
private boolean multipleSelection;
private boolean showHiddenFiles;
private boolean sortMixFilesAndDirs;
private boolean sortIgnoreCase;
private boolean doubleClickClose;
private boolean requestRunning;
private boolean maybeReloadRequestRunning;
private Stack<String> historyStack = new Stack<String>();
private Stack<String> nextDirectoryStack = new Stack<String>();
}

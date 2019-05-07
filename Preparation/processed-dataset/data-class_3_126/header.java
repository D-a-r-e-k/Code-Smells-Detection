void method0() { 
//-------------------------------------------------------------------------- 
//   Constants: 
//-------------------------------------------------------------------------- 
public static final String DETAILED_VIEW = "Detailed";
//    public static final String STANDARD_VIEW = "Standard"; 
//    public static final String COMPACT_VIEW = "Compact"; 
//-------------------------------------------------------------------------- 
//   Protected Variables: 
//-------------------------------------------------------------------------- 
protected JFrame _logMonitorFrame;
protected int _logMonitorFrameWidth = 550;
protected int _logMonitorFrameHeight = 500;
protected LogTable _table;
protected CategoryExplorerTree _categoryExplorerTree;
protected String _searchText;
protected String _NDCTextFilter = "";
protected LogLevel _leastSevereDisplayedLogLevel = LogLevel.DEBUG;
protected JScrollPane _logTableScrollPane;
protected JLabel _statusLabel;
protected Object _lock = new Object();
protected JComboBox _fontSizeCombo;
protected int _fontSize = 10;
protected String _fontName = "Dialog";
protected String _currentView = DETAILED_VIEW;
protected boolean _loadSystemFonts = false;
protected boolean _trackTableScrollPane = true;
protected Dimension _lastTableViewportSize;
protected boolean _callSystemExitOnClose = false;
protected List _displayedLogBrokerProperties = new Vector();
protected Map _logLevelMenuItems = new HashMap();
protected Map _logTableColumnMenuItems = new HashMap();
protected List _levels = null;
protected List _columns = null;
protected boolean _isDisposed = false;
protected ConfigurationManager _configurationManager = null;
protected MRUFileManager _mruFileManager = null;
protected File _fileLocation = null;
}

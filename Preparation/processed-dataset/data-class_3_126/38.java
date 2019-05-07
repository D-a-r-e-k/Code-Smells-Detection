protected void initComponents() {
    // 
    // Configure the Frame. 
    // 
    _logMonitorFrame = new JFrame("LogFactor5");
    _logMonitorFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    String resource = "/org/apache/log4j/lf5/viewer/images/lf5_small_icon.gif";
    URL lf5IconURL = getClass().getResource(resource);
    if (lf5IconURL != null) {
        _logMonitorFrame.setIconImage(new ImageIcon(lf5IconURL).getImage());
    }
    updateFrameSize();
    // 
    // Configure the LogTable. 
    // 
    JTextArea detailTA = createDetailTextArea();
    JScrollPane detailTAScrollPane = new JScrollPane(detailTA);
    _table = new LogTable(detailTA);
    setView(_currentView, _table);
    _table.setFont(new Font(_fontName, Font.PLAIN, _fontSize));
    _logTableScrollPane = new JScrollPane(_table);
    if (_trackTableScrollPane) {
        _logTableScrollPane.getVerticalScrollBar().addAdjustmentListener(new TrackingAdjustmentListener());
    }
    // Configure the SplitPane between the LogTable & DetailTextArea 
    // 
    JSplitPane tableViewerSplitPane = new JSplitPane();
    tableViewerSplitPane.setOneTouchExpandable(true);
    tableViewerSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    tableViewerSplitPane.setLeftComponent(_logTableScrollPane);
    tableViewerSplitPane.setRightComponent(detailTAScrollPane);
    // Make sure to do this last.. 
    //tableViewerSplitPane.setDividerLocation(1.0); Doesn't work 
    //the same under 1.2.x & 1.3 
    // "350" is a magic number that provides the correct default 
    // behaviour under 1.2.x & 1.3.  For example, bumping this 
    // number to 400, causes the pane to be completely open in 1.2.x 
    // and closed in 1.3 
    tableViewerSplitPane.setDividerLocation(350);
    // 
    // Configure the CategoryExplorer 
    // 
    _categoryExplorerTree = new CategoryExplorerTree();
    _table.getFilteredLogTableModel().setLogRecordFilter(createLogRecordFilter());
    JScrollPane categoryExplorerTreeScrollPane = new JScrollPane(_categoryExplorerTree);
    categoryExplorerTreeScrollPane.setPreferredSize(new Dimension(130, 400));
    // Load most recently used file list 
    _mruFileManager = new MRUFileManager();
    // 
    // Configure the SplitPane between the CategoryExplorer & (LogTable/Detail) 
    // 
    JSplitPane splitPane = new JSplitPane();
    splitPane.setOneTouchExpandable(true);
    splitPane.setRightComponent(tableViewerSplitPane);
    splitPane.setLeftComponent(categoryExplorerTreeScrollPane);
    // Do this last. 
    splitPane.setDividerLocation(130);
    // 
    // Add the MenuBar, StatusArea, CategoryExplorer|LogTable to the 
    // LogMonitorFrame. 
    // 
    _logMonitorFrame.getRootPane().setJMenuBar(createMenuBar());
    _logMonitorFrame.getContentPane().add(splitPane, BorderLayout.CENTER);
    _logMonitorFrame.getContentPane().add(createToolBar(), BorderLayout.NORTH);
    _logMonitorFrame.getContentPane().add(createStatusArea(), BorderLayout.SOUTH);
    makeLogTableListenToCategoryExplorer();
    addTableModelProperties();
    // 
    // Configure ConfigurationManager 
    // 
    _configurationManager = new ConfigurationManager(this, _table);
}

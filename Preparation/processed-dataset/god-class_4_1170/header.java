void method0() { 
private static final int FRAME_WIDTH = 850;
private static final int FRAME_HEIGHT = 550;
private final MainFrame _mainFrame;
//  private JSplitPane _splitPane; 
private final JTree _tree;
private final DefaultTreeModel _treeModel;
private final PanelTreeNode _rootNode;
private final JButton _okButton;
private final JButton _applyButton;
private final JButton _cancelButton;
//  private final JButton _saveSettingsButton; 
private final JPanel _mainPanel;
private final JFileChooser _fileOptionChooser;
private final JFileChooser _browserChooser;
private final JFileChooser _jarChooser;
private final DirectoryChooser _dirChooser;
private final ConfigOptionListeners.RequiresInteractionsRestartListener<Boolean> _junitLocationEnabledListener;
private final ConfigOptionListeners.RequiresInteractionsRestartListener<File> _junitLocationListener;
private final ConfigOptionListeners.RequiresInteractionsRestartListener<String> _concJUnitChecksEnabledListener;
private final ConfigOptionListeners.RequiresInteractionsRestartListener<File> _rtConcJUnitLocationListener;
private StringOptionComponent javadocCustomParams;
protected final String SEPS = " \t\n-,;.(";
private OptionComponent.ChangeListener _changeListener = new OptionComponent.ChangeListener() {

    public Object value(Object oc) {
        _applyButton.setEnabled(true);
        return null;
    }
};
/** Thunk that calls _cancel. */
protected final Runnable1<WindowEvent> CANCEL = new Runnable1<WindowEvent>() {

    public void run(WindowEvent e) {
        cancel();
    }
};
}

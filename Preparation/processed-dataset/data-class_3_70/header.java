void method0() { 
// gui components 
AboutDialog aboutDialog = new AboutDialog(this);
PreferencesDialog optionsDialog = new PreferencesDialog(this);
WaitDialog waitDialog = new WaitDialog(this);
AccountChooser accountChooser = new AccountChooser(this);
CategoryPanel categoryPanel = new CategoryPanel();
AccountPanel accountPanel = new AccountPanel();
AccountBalancesReportPanel accountBalancesReportPanel = new AccountBalancesReportPanel();
IncomeExpenseReportPanel incomeExpenseReportPanel = new IncomeExpenseReportPanel();
JFileChooser fileChooser = new JFileChooser();
JFileChooser qifFileChooser = new JFileChooser();
JFileChooser mt940FileChooser = new JFileChooser();
FileFormat qif = new QIF(this, accountChooser);
FileFormat mt940 = new MT940(this, accountChooser);
MenuBar menuBar = new MenuBar();
ToolBar toolBar = new ToolBar();
JTree navigationTree = new JTree();
JTabbedPane jTabbedPane1 = new JTabbedPane();
JPanel emptyPanel = new JPanel();
JSplitPane splitPane = new JSplitPane();
JScrollPane navigationScrollPane = new JScrollPane();
JPopupMenu navigationPopup = new JPopupMenu();
JMenuItem newAccountItem = new JMenuItem();
JMenuItem deleteAccountItem = new JMenuItem();
PropertyChangeListener accountNameListener = new PropertyChangeListener() {

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof Account && evt.getPropertyName().equals("name"))
            accountNameChanged();
    }
};
String title = "JMoney " + GENERAL.getString("Version");
JMoneyFileFilter jmoneyFileFilter = new JMoneyFileFilter();
FileFilter qifFileFilter = qif.fileFilter();
FileFilter mt940FileFilter = mt940.fileFilter();
// user propertes 
Properties properties = new Properties();
UserProperties userProperties = new UserProperties();
File propertiesFile;
String dateFormat;
EditableMetalTheme theme;
// session specific 
Session session;
NavigationTreeModel navigator;
File sessionFile;
}

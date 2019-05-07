public void initProperties() throws Exception {
    // read properties 
    File homeDir = FileSystemView.getFileSystemView().getDefaultDirectory();
    File jMoneyDir = new File(homeDir, ".jmoney");
    if (!jMoneyDir.exists()) {
        jMoneyDir.mkdir();
    }
    if (jMoneyDir.isDirectory()) {
        propertiesFile = new File(jMoneyDir, "preferences.txt");
        if (!propertiesFile.exists()) {
            propertiesFile.createNewFile();
        }
    }
    InputStream in = new FileInputStream(propertiesFile);
    properties.load(in);
    userProperties.setProperties(properties);
    accountPanel.getEntriesPanel().getEntryListItemLabels().setUserProperties(userProperties);
    accountPanel.getEntriesPanel().setDateFormat(userProperties.getDateFormat());
    accountPanel.getEntriesPanel().setEntryStyle(userProperties.getEntryStyle());
    accountPanel.getEntriesPanel().setEntryOrder(userProperties.getEntryOrderField(), userProperties.getEntryOrder());
    Account.setDefaultCurrencyCode(userProperties.getDefaultCurrency());
    userProperties.addPropertyChangeListener(new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent event) {
            if (event.getPropertyName().equals("lookAndFeel")) {
                initLookAndFeel();
            } else if (event.getPropertyName().equals("dateFormat")) {
                accountPanel.getEntriesPanel().setDateFormat(userProperties.getDateFormat());
            } else if (event.getPropertyName().equals("defaultCurrency")) {
                Account.setDefaultCurrencyCode(userProperties.getDefaultCurrency());
                updateUIs();
            } else if (event.getPropertyName().equals("entryStyle")) {
                accountPanel.getEntriesPanel().setEntryStyle(userProperties.getEntryStyle());
            } else if (event.getPropertyName().equals("entryOrder")) {
                accountPanel.getEntriesPanel().setEntryOrder(userProperties.getEntryOrderField(), userProperties.getEntryOrder());
            }
        }
    });
    // init window geometry and position 
    int x = 0, y = 0, w = 800, h = 600;
    try {
        x = Integer.parseInt(properties.getProperty("locationX"));
        y = Integer.parseInt(properties.getProperty("locationY"));
        w = Integer.parseInt(properties.getProperty("width"));
        h = Integer.parseInt(properties.getProperty("height"));
    } catch (NumberFormatException ex) {
    }
    this.setLocation(x, y);
    this.setSize(w, h);
    initLookAndFeel();
    // init current (last opened) file 
    String filename = properties.getProperty("currentFile");
    if (filename == null || filename.equals(""))
        return;
    setSessionFile(new File(filename));
    readSession();
}

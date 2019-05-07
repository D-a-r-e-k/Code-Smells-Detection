protected JToolBar createToolBar() {
    JToolBar tb = new JToolBar();
    tb.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
    JComboBox fontCombo = new JComboBox();
    JComboBox fontSizeCombo = new JComboBox();
    _fontSizeCombo = fontSizeCombo;
    ClassLoader cl = this.getClass().getClassLoader();
    if (cl == null) {
        cl = ClassLoader.getSystemClassLoader();
    }
    URL newIconURL = cl.getResource("org/apache/log4j/lf5/viewer/" + "images/channelexplorer_new.gif");
    ImageIcon newIcon = null;
    if (newIconURL != null) {
        newIcon = new ImageIcon(newIconURL);
    }
    JButton newButton = new JButton("Clear Log Table");
    if (newIcon != null) {
        newButton.setIcon(newIcon);
    }
    newButton.setToolTipText("Clear Log Table.");
    //newButton.setBorder(BorderFactory.createEtchedBorder()); 
    newButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _table.clearLogRecords();
            _categoryExplorerTree.getExplorerModel().resetAllNodeCounts();
            updateStatusLabel();
            clearDetailTextArea();
            LogRecord.resetSequenceNumber();
        }
    });
    Toolkit tk = Toolkit.getDefaultToolkit();
    // This will actually grab all the fonts 
    String[] fonts;
    if (_loadSystemFonts) {
        fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    } else {
        fonts = tk.getFontList();
    }
    for (int j = 0; j < fonts.length; j++) {
        fontCombo.addItem(fonts[j]);
    }
    fontCombo.setSelectedItem(_fontName);
    fontCombo.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            JComboBox box = (JComboBox) e.getSource();
            String font = (String) box.getSelectedItem();
            _table.setFont(new Font(font, Font.PLAIN, _fontSize));
            _fontName = font;
        }
    });
    fontSizeCombo.addItem("8");
    fontSizeCombo.addItem("9");
    fontSizeCombo.addItem("10");
    fontSizeCombo.addItem("12");
    fontSizeCombo.addItem("14");
    fontSizeCombo.addItem("16");
    fontSizeCombo.addItem("18");
    fontSizeCombo.addItem("24");
    fontSizeCombo.setSelectedItem(String.valueOf(_fontSize));
    fontSizeCombo.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            JComboBox box = (JComboBox) e.getSource();
            String size = (String) box.getSelectedItem();
            int s = Integer.valueOf(size).intValue();
            setFontSizeSilently(s);
            refreshDetailTextArea();
            _fontSize = s;
        }
    });
    tb.add(new JLabel(" Font: "));
    tb.add(fontCombo);
    tb.add(fontSizeCombo);
    tb.addSeparator();
    tb.addSeparator();
    tb.add(newButton);
    newButton.setAlignmentY(0.5f);
    newButton.setAlignmentX(0.5f);
    fontCombo.setMaximumSize(fontCombo.getPreferredSize());
    fontSizeCombo.setMaximumSize(fontSizeCombo.getPreferredSize());
    return (tb);
}

/** Add all of the components for the Positions panel of the preferences window. */
private void _setupPositionsPanel(ConfigPanel panel) {
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.WINDOW_STORE_POSITION, "Save Main Window Position", this, "Whether to save and restore the size and position of the main window.", false).setEntireColumn(true));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_CLIPBOARD_HISTORY_STORE_POSITION, "Save \"Clipboard History\" Dialog Position", this, "Whether to save and restore the size and position of the \"Clipboard History\" dialog.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetClipboardHistoryDialogPosition();
        }
    }, "Reset \"Clipboard History\" Dialog Position and Size", this, "This resets the dialog position and size to its default values."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_GOTOFILE_STORE_POSITION, "Save \"Go to File\" Dialog Position", this, "Whether to save and restore the size and position of the \"Go to File\" dialog.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetGotoFileDialogPosition();
        }
    }, "Reset \"Go to File\" Dialog Position and Size", this, "This resets the dialog position and size to its default values."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_COMPLETE_WORD_STORE_POSITION, "Save \"Auto-Complete Word\" Dialog Position", this, "Whether to save and restore the size and position of the \"Auto-Complete Word\" dialog.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetCompleteWordDialogPosition();
        }
    }, "Reset \"Auto-Complete Word\" Dialog Position and Size", this, "This resets the dialog position and size to its default values."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_JAROPTIONS_STORE_POSITION, "Save \"Create Jar File from Project\" Dialog Position", this, "Whether to save and restore the position of the \"Create Jar File from Project\" dialog.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetJarOptionsDialogPosition();
        }
    }, "Reset \"Create Jar File from Project\" Dialog Position", this, "This resets the dialog position to its default values."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_OPENJAVADOC_STORE_POSITION, "Save \"Open Javadoc\" Dialog Position", this, "Whether to save and restore the size and position of the \"Open Javadoc\" dialog.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetOpenJavadocDialogPosition();
        }
    }, "Reset \"Open Javadoc\" Dialog Position and Size", this, "This resets the dialog position and size to its default values."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_AUTOIMPORT_STORE_POSITION, "Save \"Auto Import\" Dialog Position", this, "Whether to save and restore the size and position of the \"Auto Import\" dialog.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetAutoImportDialogPosition();
        }
    }, "Reset \"Auto Import\" Dialog Position and Size", this, "This resets the dialog position and size to its default values."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_EXTERNALPROCESS_STORE_POSITION, "Save \"Execute External Process\" Dialog Position", this, "Whether to save and restore the position of the \"Execute External Process\" dialog.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetExecuteExternalProcessPosition();
        }
    }, "Reset \"Execute External Process\" Dialog Position", this, "This resets the dialog position to its default values."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_EDITEXTERNALPROCESS_STORE_POSITION, "Save \"Edit External Process\" Dialog Position", this, "Whether to save and restore the position of the \"Edit External Process\" dialog.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetEditExternalProcessPosition();
        }
    }, "Reset \"Execute External Process\" Dialog Position", this, "This resets the dialog position to its default values."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_OPENJAVADOC_STORE_POSITION, "Save \"Open Javadoc\" Dialog Position", this, "Whether to save and restore the position of the \"Open Javadoc\" dialog.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetOpenJavadocDialogPosition();
        }
    }, "Reset \"Open Javadoc\" Dialog Position", this, "This resets the dialog position to its default values."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_TABBEDPANES_STORE_POSITION, "Save \"Tabbed Panes\" Window Position", this, "Whether to save and restore the position of the \"Tabbed Panes\" window.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetTabbedPanesFrame();
        }
    }, "Reset \"Tabbed Panes\" Window Position", this, "This resets the window position to its default values."));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DETACH_TABBEDPANES, "Detach Tabbed Panes", this, "Whether to detach the tabbed panes and display them in a separate window.", false).setEntireColumn(true));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_DEBUGFRAME_STORE_POSITION, "Save \"Debugger\" Window Position", this, "Whether to save and restore the position of the \"Debugger\" window.", false).setEntireColumn(true));
    addOptionComponent(panel, new ButtonComponent(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _mainFrame.resetDebugFrame();
        }
    }, "Reset \"Debugger\" Window Position", this, "This resets the window position to its default values."));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DETACH_DEBUGGER, "Detach Debugger", this, "Whether to detach the debugger and display it in a separate window.", false).setEntireColumn(true));
    panel.displayComponents();
}

/** Add all of the components for the Display Options panel of the preferences window. */
private void _setupDisplayPanel(ConfigPanel panel) {
    final ForcedChoiceOptionComponent lookAndFeelComponent = new ForcedChoiceOptionComponent(OptionConstants.LOOK_AND_FEEL, "Look and Feel", this, "Changes the general appearance of DrJava.");
    addOptionComponent(panel, lookAndFeelComponent);
    final ForcedChoiceOptionComponent plasticComponent = new ForcedChoiceOptionComponent(OptionConstants.PLASTIC_THEMES, "Plastic Theme", this, "Pick the theme to be used by the Plastic family of Look and Feels");
    lookAndFeelComponent.addChangeListener(new OptionComponent.ChangeListener() {

        public Object value(Object oc) {
            plasticComponent.getComponent().setEnabled(lookAndFeelComponent.getCurrentComboBoxValue().startsWith("com.jgoodies.looks.plastic."));
            return null;
        }
    });
    plasticComponent.getComponent().setEnabled(lookAndFeelComponent.getCurrentComboBoxValue().startsWith("com.jgoodies.looks.plastic."));
    addOptionComponent(panel, plasticComponent);
    //ToolbarOptionComponent is a degenerate option component 
    addOptionComponent(panel, new ToolbarOptionComponent("Toolbar Buttons", this, "How to display the toolbar buttons."));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.LINEENUM_ENABLED, "Show All Line Numbers", this, "Whether to show line numbers on the left side of the Definitions Pane."));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.SHOW_SOURCE_WHEN_SWITCHING, "Show sample of source code when fast switching", this, "Whether to show a sample of the source code under the document's filename when fast switching documents."));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.SHOW_CODE_PREVIEW_POPUPS, "Show Code Preview Popups", this, "<html>Whether to show a popup window with a code preview when the mouse is hovering<br>" + "over an item in the Breakpoints, Bookmarks and Find All panes.</html>"));
    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.CLIPBOARD_HISTORY_SIZE, "Size of Clipboard History", this, "Determines how many entries are kept in the clipboard history."));
    BooleanOptionComponent checkbox = new BooleanOptionComponent(OptionConstants.DIALOG_GOTOFILE_FULLY_QUALIFIED, "<html><p align=\"right\">" + StringOps.splitStringAtWordBoundaries("Display Fully-Qualified Class Names in \"Go to File\" Dialog", 40, "<br>", SEPS) + "</p></html>", this, "<html>Whether to also display fully-qualified class names in the \"Go to File\" dialog.<br>" + "Enabling this option on network drives might cause the dialog to display after a slight delay.</html>");
    addOptionComponent(panel, checkbox);
    checkbox = new BooleanOptionComponent(OptionConstants.DIALOG_COMPLETE_SCAN_CLASS_FILES, "<html><p align=\"right\">" + StringOps.splitStringAtWordBoundaries("Scan Class Files After Each Compile for Auto-Completion and Auto-Import", 40, "<br>", SEPS) + "</p></html>", this, "<html>Whether to scan the class files after a compile to generate class names<br>" + "used for auto-completion and auto-import.<br>" + "Enabling this option will slow compiles down.</html>");
    addOptionComponent(panel, checkbox);
    checkbox = new BooleanOptionComponent(OptionConstants.DIALOG_COMPLETE_JAVAAPI, "<html><p align=\"right\">" + StringOps.splitStringAtWordBoundaries("Consider Java API Classes for Auto-Completion", 40, "<br>", SEPS) + "</p></html>", this, "Whether to use the names of the Java API classes for auto-completion as well.");
    addOptionComponent(panel, checkbox);
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    final BooleanOptionComponent drmComponent = new BooleanOptionComponent(OptionConstants.DISPLAY_RIGHT_MARGIN, "Display right margin", this, "Whether to display a line at the right margin.");
    addOptionComponent(panel, drmComponent);
    final IntegerOptionComponent rmcComponent = new IntegerOptionComponent(OptionConstants.RIGHT_MARGIN_COLUMNS, "Right Margin Position", this, "The number of columns after which the right margin is displayed.");
    addOptionComponent(panel, rmcComponent);
    OptionComponent.ChangeListener drmListener = new OptionComponent.ChangeListener() {

        public Object value(Object oc) {
            rmcComponent.getComponent().setEnabled(drmComponent.getComponent().isSelected());
            return null;
        }
    };
    drmComponent.addChangeListener(drmListener);
    drmListener.value(drmComponent);
    panel.displayComponents();
}

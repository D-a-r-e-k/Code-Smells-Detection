/** Add all of the components for the Interactions panel of the preferences window. */
private void _setupInteractionsPanel(ConfigPanel panel) {
    final DirectoryOptionComponent wdComponent = new DirectoryOptionComponent(OptionConstants.FIXED_INTERACTIONS_DIRECTORY, "Interactions Working Directory", this, "<html>Working directory for the Interactions Pane (unless<br>" + "a project working directory has been set).</html>", _dirChooser);
    addOptionComponent(panel, wdComponent);
    final BooleanOptionComponent stickyComponent = new BooleanOptionComponent(OptionConstants.STICKY_INTERACTIONS_DIRECTORY, "<html><p align=\"right\">" + StringOps.splitStringAtWordBoundaries("Restore last working directory of the Interactions pane on start up", 33, "<br>", SEPS), this, "<html>Whether to restore the last working directory of the Interaction pane on start up,<br>" + "or to always use the value of the \"user.home\" Java property<br>" + "(currently " + System.getProperty("user.home") + ").");
    addOptionComponent(panel, stickyComponent);
    OptionComponent.ChangeListener wdListener = new OptionComponent.ChangeListener() {

        public Object value(Object oc) {
            File f = wdComponent.getComponent().getFileFromField();
            boolean enabled = (f == null) || (f.equals(FileOps.NULL_FILE));
            stickyComponent.getComponent().setEnabled(enabled);
            return null;
        }
    };
    wdComponent.addChangeListener(wdListener);
    wdListener.value(wdComponent);
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.SMART_RUN_FOR_APPLETS_AND_PROGRAMS, "Smart Run Command", this, "<html>Whether the Run button and meni item should automatically detect<br>" + "applets and ACM Java Task Force programs (subclasses of acm.program.Program).</html>"));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.HISTORY_MAX_SIZE, "Size of Interactions History", this, "The number of interactions to remember in the history."));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_AUTOIMPORT_ENABLED, "Enable the \"Auto Import\" Dialog", this, "<html>Whether DrJava should open the \"Auto Import\" dialog when<br>" + "an undefined class is encountered in the Interactions Pane.</html>"));
    VectorStringOptionComponent autoImportClasses = new VectorStringOptionComponent(OptionConstants.INTERACTIONS_AUTO_IMPORT_CLASSES, "Classes to Auto-Import", this, "<html>List of classes to auto-import every time the<br>" + "Interaction Pane is reset or started. Examples:<br><br>" + "java.io.File<br>" + "java.util.*</html>") {

        protected boolean verify(String s) {
            boolean result = true;
            // verify that the string contains only Java identifier characters, dots and stars 
            for (int i = 0; i < s.length(); ++i) {
                char ch = s.charAt(i);
                if ((ch != '.') && (ch != '*') && (!Character.isJavaIdentifierPart(ch))) {
                    result = false;
                    break;
                }
            }
            if (!result) {
                JOptionPane.showMessageDialog(ConfigFrame.this, "This is not a valid class name:\n" + s, "Error Adding Class Name", JOptionPane.ERROR_MESSAGE);
            }
            return result;
        }
    };
    addOptionComponent(panel, autoImportClasses);
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    addOptionComponent(panel, new ForcedChoiceOptionComponent(OptionConstants.DYNAMICJAVA_ACCESS_CONTROL, "Enforce access control", this, "What kind of access control should DrJava enforce in the Interactions Pane?"));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DYNAMICJAVA_REQUIRE_SEMICOLON, "Require Semicolon", this, "<html>Whether DrJava should require a semicolon at the<br>" + "end of a statement in the Interactions Pane.</html>"));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DYNAMICJAVA_REQUIRE_VARIABLE_TYPE, "Require Variable Type", this, "<html>Whether DrJava should require a variable type for<br>" + "variable declarations in the Interactions Pane.</html>"));
    panel.displayComponents();
}

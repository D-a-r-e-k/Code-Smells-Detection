/** Adds all of the components for the Miscellaneous panel of the preferences window. */
private void _setupMiscPanel(ConfigPanel panel) {
    /* Dialog box options */
    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.INDENT_LEVEL, "Indent Level", this, "The number of spaces to use for each level of indentation."));
    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.RECENT_FILES_MAX_SIZE, "Recent Files List Size", this, "<html>The number of files to remember in<br>" + "the recently used files list in the File menu.</html>"));
    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.BROWSER_HISTORY_MAX_SIZE, "Maximum Size of Browser History", this, "Determines how many entries are kept in the browser history."));
    /* Check box options */
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.AUTO_CLOSE_COMMENTS, "Automatically Close Block Comments", this, "<html>Whether to automatically insert a closing comment tag (\"*/\")<br>" + "when the enter key is pressed after typing a new block comment<br>" + "tag (\"/*\" or \"/**\").</html>"));
    String runWithAssertMsg = "<html>Whether to execute <code>assert</code> statements in classes running in the interactions pane.</html>";
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.RUN_WITH_ASSERT, "Enable Assert Statement Execution", this, runWithAssertMsg));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.BACKUP_FILES, "Keep Emacs-style Backup Files", this, "<html>Whether DrJava should keep a backup copy of each file that<br>" + "the user modifies, saved with a '~' at the end of the filename.</html>"));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.RESET_CLEAR_CONSOLE, "Clear Console After Interactions Reset", this, "Whether to clear the Console output after resetting the Interactions Pane."));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.FIND_REPLACE_FOCUS_IN_DEFPANE, "Focus on the definitions pane after find/replace", this, "<html>Whether to focus on the definitions pane after executing a find/replace operation.<br>" + "If this is not selected, the focus will be in the Find/Replace pane.</html>"));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DRJAVA_USE_FORCE_QUIT, "Forcefully Quit DrJava", this, "<html>On some platforms, DrJava does not shut down properly when files are open<br>" + "(namely tablet PCs). Check this option to force DrJava to close.</html>"));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.REMOTE_CONTROL_ENABLED, "Enable Remote Control", this, "<html>Whether DrJava should listen to a socket (see below) so it<br>" + "can be remote controlled and told to open files.<br>" + "(Changes will not be applied until DrJava is restarted.)</html>"));
    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.REMOTE_CONTROL_PORT, "Remote Control Port", this, "<html>A running instance of DrJava can be remote controlled and<br>" + "told to open files. This specifies the port used for remote control.<br>" + "(Changes will not be applied until DrJava is restarted.)</html>"));
    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.FOLLOW_FILE_DELAY, "Follow File Delay", this, "<html>The delay in milliseconds that has to elapse before DrJava will check<br>" + "if a file that is being followed or the output of an external process has changed.</html>"));
    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.FOLLOW_FILE_LINES, "Maximum Lines in \"Follow File\" Window", this, "<html>The maximum number of lines to keep in a \"Follow File\"<br>" + "or \"External Process\" pane. Enter 0 for unlimited.</html>"));
    // Any lightweight parsing has been disabled until we have something that is beneficial and works better in the background. 
    //    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.LIGHTWEIGHT_PARSING_ENABLED,  
    //                                                  "Perform lightweight parsing", this, 
    //                                                  "<html>Whether to continuously parse the source file for useful information.<br>" + 
    //                                                  "Enabling this option might introduce delays when editing files.<html>")); 
    //    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.DIALOG_LIGHTWEIGHT_PARSING_DELAY, "Light-weight parsing delay in milliseconds", this, 
    //                                                  "The amount of time DrJava will wait after the last keypress before beginning to parse.")); 
    panel.displayComponents();
}

/** Adds all of the components for the Prompts panel of the preferences window. */
private void _setupNotificationsPanel(ConfigPanel panel) {
    // Quit 
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.QUIT_PROMPT, "Prompt Before Quit", this, "Whether DrJava should prompt the user before quitting.", false).setEntireColumn(true));
    // Interactions 
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.INTERACTIONS_RESET_PROMPT, "Prompt Before Resetting Interactions Pane", this, "<html>Whether DrJava should prompt the user before<br>" + "manually resetting the interactions pane.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.INTERACTIONS_EXIT_PROMPT, "Prompt if Interactions Pane Exits Unexpectedly", this, "<html>Whether DrJava should show a dialog box if a program<br>" + "in the Interactions Pane exits without the user clicking Reset.</html>", false).setEntireColumn(true));
    // Javadoc 
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.JAVADOC_PROMPT_FOR_DESTINATION, "Prompt for Javadoc Destination", this, "<html>Whether Javadoc should always prompt the user<br>" + "to select a destination directory.</html>", false).setEntireColumn(true));
    // Clean 
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.PROMPT_BEFORE_CLEAN, "Prompt before Cleaning Build Directory", this, "<html>Whether DrJava should prompt before cleaning the<br>" + "build directory of a project</html>", false).setEntireColumn(true));
    // Prompt to change the language level extensions (.dj0/.dj1->.dj, .dj2->.java) 
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.PROMPT_RENAME_LL_FILES, "Prompt to Rename Old Language Level Files When Saving", this, "<html>Whether DrJava should prompt the user to rename old language level files.<br>" + "DrJava suggests to rename .dj0 and .dj1 files to .dj, and .dj2 files to .java.</html>", false).setEntireColumn(true));
    // Save before X 
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.ALWAYS_SAVE_BEFORE_COMPILE, "Automatically Save Before Compiling", this, "<html>Whether DrJava should automatically save before<br>" + "recompiling or ask the user each time.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.ALWAYS_COMPILE_BEFORE_JUNIT, "Automatically Compile Before Testing", this, "<html>Whether DrJava should automatically compile before<br>" + "testing with JUnit or ask the user each time.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.ALWAYS_SAVE_BEFORE_JAVADOC, "Automatically Save Before Generating Javadoc", this, "<html>Whether DrJava should automatically save before<br>" + "generating Javadoc or ask the user each time.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.ALWAYS_COMPILE_BEFORE_JAVADOC, "Automatically Compile Before Generating Javadoc", this, "<html>Whether DrJava should automatically compile before<br>" + "generating Javadoc or ask the user each time.</html>", false).setEntireColumn(true));
    // These are very problematic features, and so are disabled for the forseeable future. 
    //    addOptionComponent(panel,  
    //                       new BooleanOptionComponent(OptionConstants.ALWAYS_SAVE_BEFORE_RUN,  
    //                                                  "Automatically Save and Compile Before Running Main Method",  
    //                                                  this, 
    //                                                  "<html>Whether DrJava automatically saves and compiles before running<br>" + 
    //                                                  "a document's main method or explicitly asks the user each time.</html>")); 
    //    addOptionComponent(panel,  
    //                       new BooleanOptionComponent(OptionConstants.ALWAYS_SAVE_BEFORE_DEBUG,  
    //                                                  "Automatically Save and Compile Before Debugging",  
    //                                                  this, 
    //                                                  "<html>Whether DrJava automatically saves and compiles before<br>" + 
    //                                                  "debugging or explicitly asks the user each time.</html>")); 
    // Warnings 
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.WARN_BREAKPOINT_OUT_OF_SYNC, "Warn on Breakpoint if Out of Sync", this, "<html>Whether DrJava should warn the user if the class file<br>" + "is out of sync before setting a breakpoint in that file.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.WARN_DEBUG_MODIFIED_FILE, "Warn if Debugging Modified File", this, "<html>Whether DrJava should warn the user if the file being<br>" + "debugged has been modified since its last save.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.WARN_CHANGE_LAF, "Warn to Restart to Change Look and Feel", this, "<html>Whether DrJava should warn the user that look and feel<br>" + "changes will not be applied until DrJava is restarted.</html>.", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.WARN_CHANGE_THEME, "Warn to Restart to Change Theme", this, "<html>Whether DrJava should warn the user that theme<br>" + "changes will not be applied until DrJava is restarted.</html>.", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.WARN_CHANGE_DCP, "Warn to Restart to Change Default Compiler Preference", this, "<html>Whether DrJava should warn the user that default compiler preference<br>" + "changes will not be applied until DrJava is restarted.</html>.", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.WARN_CHANGE_MISC, "Warn to Restart to Change Preferences (other)", this, "<html>Whether DrJava should warn the user that preference<br>" + "changes will not be applied until DrJava is restarted.</html>.", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.WARN_CHANGE_INTERACTIONS, "Warn to Reset to Change Interactions", this, "<html>Whether DrJava should warn the user that preference<br>" + "changes will not be applied until the Interactions Pane<br>" + "is reset.</html>.", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.WARN_PATH_CONTAINS_POUND, "Warn if File's Path Contains a '#' Symbol", this, "<html>Whether DrJava should warn the user if the file being<br>" + "saved has a path that contains a '#' symbol.<br>" + "Users cannot use such files in the Interactions Pane<br>" + "because of a bug in Java.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DIALOG_DRJAVA_ERROR_POPUP_ENABLED, "Show a notification window when the first DrJava error occurs", this, "<html>Whether to show a notification window when the first DrJava error occurs.<br>" + "If this is disabled, only the \"DrJava Error\" button will appear.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.WARN_IF_COMPIZ, "Warn If Compiz Detected", this, "<html>Whether DrJava should warn the user if Compiz is running.<br>" + "Compiz and Java Swing are incompatible and can lead to crashes.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</html>", this, true));
    addOptionComponent(panel, new ForcedChoiceOptionComponent(OptionConstants.DELETE_LL_CLASS_FILES, "Delete language level class files?", this, "Whether DrJava should delete class files in directories with language level files."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</html>", this, true));
    addOptionComponent(panel, new ForcedChoiceOptionComponent(OptionConstants.NEW_VERSION_NOTIFICATION, "Check for new versions?", this, "Whether DrJava should check for new versions on drjava.org."));
    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.NEW_VERSION_NOTIFICATION_DAYS, "Days between new version check", this, "The number of days between automatic new version checks."));
    panel.displayComponents();
}

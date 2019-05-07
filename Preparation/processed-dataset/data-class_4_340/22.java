/** Add all of the components for the Debugger panel of the preferences window. */
private void _setupDebugPanel(ConfigPanel panel) {
    if (!_mainFrame.getModel().getDebugger().isAvailable()) {
        // Explain how to use debugger 
        String howto = "\nThe debugger is not currently available. To use the debugger,\n" + "you can enter the location of the tools.jar file in the\n" + "\"Resource Locations\" pane, in case DrJava does not automatically find it.\n" + "See the user documentation for more details.\n";
        LabelComponent label = new LabelComponent(howto, this);
        label.setEntireColumn(true);
        addOptionComponent(panel, label);
    }
    VectorFileOptionComponent sourcePath = new VectorFileOptionComponent(OptionConstants.DEBUG_SOURCEPATH, "Sourcepath", this, "<html>Any directories in which to search for source<br>" + "files when stepping in the Debugger.</html>", true);
    // Source path can only include directories 
    sourcePath.getFileChooser().setFileFilter(new DirectoryFilter("Source Directories"));
    addOptionComponent(panel, sourcePath);
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DEBUG_STEP_JAVA, "Step Into Java Classes", this, "<html>Whether the Debugger should step into Java library classes,<br>" + "including java.*, javax.*, sun.*, com.sun.*, com.apple.eawt.*, and com.apple.eio.*</html>"));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DEBUG_STEP_INTERPRETER, "Step Into Interpreter Classes", this, "<html>Whether the Debugger should step into the classes<br>" + "used by the Interactions Pane (DynamicJava).</html>"));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DEBUG_STEP_DRJAVA, "Step Into DrJava Classes", this, "Whether the Debugger should step into DrJava's own class files."));
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</html>", this, true));
    addOptionComponent(panel, new VectorStringOptionComponent(OptionConstants.DEBUG_STEP_EXCLUDE, "Classes/Packages To Exclude", this, "<html>Any classes that the debuggger should not step into.<br>" + "Should be a list of fully-qualified class names.<br>" + "To exclude a package, add <code>packagename.*</code> to the list.</html>"));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DEBUG_AUTO_IMPORT, "Auto-Import after Breakpoint/Step", this, "<html>Whether the Debugger should automatically import packages<br>" + "and classes again after a breakpoint or step.</html>"));
    addOptionComponent(panel, new IntegerOptionComponent(OptionConstants.AUTO_STEP_RATE, "Auto-Step Rate in ms", this, "<html>A defined rate in ms at which the debugger automatically steps into/over each line of code.<br>" + "Value entered must be an integer value. </html>"));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.DEBUG_EXPRESSIONS_AND_METHODS_IN_WATCHES, "Allow Expressions and Method Calls in Watches", this, "<html>Whether the Debugger should allow expressions and method<br>" + "calls in watches. These may have side effects and can cause<br>" + "delays during the debug process.</html>"));
    panel.displayComponents();
}

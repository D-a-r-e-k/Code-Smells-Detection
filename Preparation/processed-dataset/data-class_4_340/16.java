/** Add all of the components for the Resource Locations panel of the preferences window. */
private void _setupResourceLocPanel(ConfigPanel panel) {
    FileOptionComponent browserLoc = new FileOptionComponent(OptionConstants.BROWSER_FILE, "Web Browser", this, "<html>Location of a web browser to use for Javadoc and Help links.<br>" + "If left blank, only the Web Browser Command will be used.<br>" + "This is not necessary if a default browser is available on your system.", _browserChooser);
    addOptionComponent(panel, browserLoc);
    StringOptionComponent browserCommand = new StringOptionComponent(OptionConstants.BROWSER_STRING, "Web Browser Command", this, "<html>Command to send to the web browser to view a web location.<br>" + "The string <code>&lt;URL&gt;</code> will be replaced with the URL address.<br>" + "This is not necessary if a default browser is available on your system.");
    addOptionComponent(panel, browserCommand);
    FileOptionComponent javacLoc = new FileOptionComponent(OptionConstants.JAVAC_LOCATION, "Tools.jar Location", this, "Optional location of the JDK's tools.jar, which contains the compiler and debugger.", _fileOptionChooser);
    javacLoc.setFileFilter(ClassPathFilter.ONLY);
    addOptionComponent(panel, javacLoc);
    BooleanOptionComponent displayAllCompilerVersions = new BooleanOptionComponent(OptionConstants.DISPLAY_ALL_COMPILER_VERSIONS, "Display All Compiler Versions", this, "Display all compiler versions, even if they have the same major version.");
    addOptionComponent(panel, displayAllCompilerVersions);
    addOptionComponent(panel, new VectorFileOptionComponent(OptionConstants.EXTRA_CLASSPATH, "Extra Classpath", this, "<html>Any directories or jar files to add to the classpath<br>" + "of the Compiler and Interactions Pane.</html>", true));
    panel.displayComponents();
}

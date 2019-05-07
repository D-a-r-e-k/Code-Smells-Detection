/** Creates all of the panels contained within the frame. */
private void _createPanels() {
    PanelTreeNode resourceLocNode = _createPanel("Resource Locations");
    _setupResourceLocPanel(resourceLocNode.getPanel());
    PanelTreeNode displayNode = _createPanel("Display Options");
    _setupDisplayPanel(displayNode.getPanel());
    PanelTreeNode fontNode = _createPanel("Fonts", displayNode);
    _setupFontPanel(fontNode.getPanel());
    PanelTreeNode colorNode = _createPanel("Colors", displayNode);
    _setupColorPanel(colorNode.getPanel());
    PanelTreeNode positionsNode = _createPanel("Window Positions", displayNode);
    _setupPositionsPanel(positionsNode.getPanel());
    PanelTreeNode keystrokesNode = _createPanel("Key Bindings");
    _setupKeyBindingsPanel(keystrokesNode.getPanel());
    PanelTreeNode compilerOptionsNode = _createPanel("Compiler Options");
    _setupCompilerPanel(compilerOptionsNode.getPanel());
    PanelTreeNode interactionsNode = _createPanel("Interactions Pane");
    _setupInteractionsPanel(interactionsNode.getPanel());
    PanelTreeNode debugNode = _createPanel("Debugger");
    _setupDebugPanel(debugNode.getPanel());
    PanelTreeNode junitNode = _createPanel("JUnit");
    _setupJUnitPanel(junitNode.getPanel());
    PanelTreeNode javadocNode = _createPanel("Javadoc");
    _setupJavadocPanel(javadocNode.getPanel());
    PanelTreeNode notificationsNode = _createPanel("Notifications");
    _setupNotificationsPanel(notificationsNode.getPanel());
    PanelTreeNode miscNode = _createPanel("Miscellaneous");
    _setupMiscPanel(miscNode.getPanel());
    PanelTreeNode fileTypesNode = _createPanel("File Types", miscNode);
    _setupFileTypesPanel(fileTypesNode.getPanel());
    PanelTreeNode jvmsNode = _createPanel("JVMs", miscNode);
    _setupJVMsPanel(jvmsNode.getPanel());
}

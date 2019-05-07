/** Add all of the components for the Font panel of the preferences window. */
private void _setupFontPanel(ConfigPanel panel) {
    addOptionComponent(panel, new FontOptionComponent(OptionConstants.FONT_MAIN, "Main Font", this, "The font used for most text in DrJava."));
    addOptionComponent(panel, new FontOptionComponent(OptionConstants.FONT_LINE_NUMBERS, "Line Numbers Font", this, "<html>The font for displaying line numbers on the left side of<br>" + "the Definitions Pane if Show All Line Numbers is enabled.<br>" + "Cannot be displayed larger than the Main Font.</html>"));
    addOptionComponent(panel, new FontOptionComponent(OptionConstants.FONT_DOCLIST, "Document List Font", this, "The font used in the list of open documents."));
    addOptionComponent(panel, new FontOptionComponent(OptionConstants.FONT_TOOLBAR, "Toolbar Font", this, "The font used in the toolbar buttons."));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.TEXT_ANTIALIAS, "Use anti-aliased text", this, "Whether to graphically smooth the text."));
    panel.displayComponents();
}

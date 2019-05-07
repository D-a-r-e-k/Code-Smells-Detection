/** Adds all of the components for the Color panel of the preferences window.
   */
private void _setupColorPanel(ConfigPanel panel) {
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_NORMAL_COLOR, "Normal Color", this, "The default color for text in the Definitions Pane."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_KEYWORD_COLOR, "Keyword Color", this, "The color for Java keywords in the Definitions Pane."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_TYPE_COLOR, "Type Color", this, "The color for classes and types in the Definitions Pane."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_COMMENT_COLOR, "Comment Color", this, "The color for comments in the Definitions Pane."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_DOUBLE_QUOTED_COLOR, "Double-quoted Color", this, "The color for quoted strings (eg. \"...\") in the Definitions Pane."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_SINGLE_QUOTED_COLOR, "Single-quoted Color", this, "The color for quoted characters (eg. 'a') in the Definitions Pane."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_NUMBER_COLOR, "Number Color", this, "The color for numbers in the Definitions Pane."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_BACKGROUND_COLOR, "Background Color", this, "The background color of the Definitions Pane.", true));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_LINE_NUMBER_COLOR, "Line Number Color", this, "The color for line numbers in the Definitions Pane."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_LINE_NUMBER_BACKGROUND_COLOR, "Line Number Background Color", this, "The background color for line numbers in the Definitions Pane.", true));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEFINITIONS_MATCH_COLOR, "Brace-matching Color", this, "The color for matching brace highlights in the Definitions Pane.", true));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.COMPILER_ERROR_COLOR, "Compiler Error Color", this, "The color for compiler error highlights in the Definitions Pane.", true));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.BOOKMARK_COLOR, "Bookmark Color", this, "The color for bookmarks in the Definitions Pane.", true));
    for (int i = 0; i < OptionConstants.FIND_RESULTS_COLORS.length; ++i) {
        addOptionComponent(panel, new ColorOptionComponent(OptionConstants.FIND_RESULTS_COLORS[i], "Find Results Color " + (i + 1), this, "A color for highlighting find results in the Definitions Pane.", true));
    }
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEBUG_BREAKPOINT_COLOR, "Debugger Breakpoint Color", this, "The color for breakpoints in the Definitions Pane.", true));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEBUG_BREAKPOINT_DISABLED_COLOR, "Disabled Debugger Breakpoint Color", this, "The color for disabled breakpoints in the Definitions Pane.", true));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEBUG_THREAD_COLOR, "Debugger Location Color", this, "The color for the location of the current suspended thread in the Definitions Pane.", true));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.SYSTEM_OUT_COLOR, "System.out Color", this, "The color for System.out in the Interactions and Console Panes."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.SYSTEM_ERR_COLOR, "System.err Color", this, "The color for System.err in the Interactions and Console Panes."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.SYSTEM_IN_COLOR, "System.in Color", this, "The color for System.in in the Interactions Pane."));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.INTERACTIONS_ERROR_COLOR, "Interactions Error Color", this, "The color for interactions errors in the Interactions Pane.", false, true));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DEBUG_MESSAGE_COLOR, "Debug Message Color", this, "The color for debugger messages in the Interactions Pane.", false, true));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.DRJAVA_ERRORS_BUTTON_COLOR, "DrJava Errors Button Background Color", this, "The background color of the \"Errors\" button used to show internal DrJava errors.", true));
    addOptionComponent(panel, new ColorOptionComponent(OptionConstants.RIGHT_MARGIN_COLOR, "Right Margin Color", this, "The color of the right margin line, if displayed.", true));
    panel.displayComponents();
}

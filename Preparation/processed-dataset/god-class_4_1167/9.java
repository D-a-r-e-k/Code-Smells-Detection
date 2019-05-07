/** Performs the "find next" command.  Package visibility to accommodate calls from MainFrame. */
void findNext() {
    _frame.updateStatusField("Finding Next");
    _machine.setSearchBackwards(false);
    _findLabelBot.setText("Next");
    _doFind();
    // updates position stored in machine before starting 
    if (DrJava.getConfig().getSetting(OptionConstants.FIND_REPLACE_FOCUS_IN_DEFPANE).booleanValue()) {
        _defPane.requestFocusInWindow();
    }
}

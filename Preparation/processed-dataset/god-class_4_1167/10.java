/** Called when user the activates "find previous" command.  Package visibility to accommodate calls from MainFrame. */
void findPrevious() {
    _frame.updateStatusField("Finding Previous");
    _machine.setSearchBackwards(true);
    _findLabelBot.setText("Prev");
    _doFind();
    if (DrJava.getConfig().getSetting(OptionConstants.FIND_REPLACE_FOCUS_IN_DEFPANE).booleanValue()) {
        _defPane.requestFocusInWindow();
    }
}

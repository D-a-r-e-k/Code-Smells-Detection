private void _replaceFindPrevious() {
    _frame.updateStatusField("Replacing and Finding Previous");
    if (isSearchBackwards() == false) {
        _machine.positionChanged();
        findPrevious();
    }
    _updateMachine();
    _machine.setFindWord(_findField.getText());
    final String replaceWord = _replaceField.getText();
    _machine.setReplaceWord(replaceWord);
    _frame.clearStatusMessage();
    // replaces the occurrence at the current position 
    boolean replaced = _machine.replaceCurrent();
    // and finds the previous word 
    if (replaced) {
        _selectFoundOrReplacedItem(replaceWord.length());
        findPrevious();
        _replaceFindPreviousButton.requestFocusInWindow();
    } else {
        _replaceAction.setEnabled(false);
        _replaceFindNextAction.setEnabled(false);
        _replaceFindPreviousAction.setEnabled(false);
        Toolkit.getDefaultToolkit().beep();
        _frame.setStatusMessage("Replace failed.");
    }
}

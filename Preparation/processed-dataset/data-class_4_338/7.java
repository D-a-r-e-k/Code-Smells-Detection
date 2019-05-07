private void _replaceFindNext() {
    _frame.updateStatusField("Replacing and Finding Next");
    if (isSearchBackwards() == true) {
        _machine.positionChanged();
        findNext();
    }
    _updateMachine();
    _machine.setFindWord(_findField.getText());
    final String replaceWord = _replaceField.getText();
    _machine.setReplaceWord(replaceWord);
    _frame.clearStatusMessage();
    // _message.setText(""); // JL 
    // replaces the occurrence at the current position 
    boolean replaced = _machine.replaceCurrent();
    // and finds the next word 
    if (replaced) {
        _selectFoundOrReplacedItem(replaceWord.length());
        findNext();
        _replaceFindNextButton.requestFocusInWindow();
    } else {
        _replaceAction.setEnabled(false);
        _replaceFindNextAction.setEnabled(false);
        _replaceFindPreviousAction.setEnabled(false);
        Toolkit.getDefaultToolkit().beep();
        _frame.setStatusMessage("Replace failed.");
    }
}

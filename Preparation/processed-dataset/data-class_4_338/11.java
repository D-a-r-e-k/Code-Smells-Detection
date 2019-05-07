private void _replace() {
    _frame.updateStatusField("Replacing");
    //    _updateMachine(); 
    _machine.setFindWord(_findField.getText());
    final String replaceWord = _replaceField.getText();
    _machine.setReplaceWord(replaceWord);
    _frame.clearStatusMessage();
    // replaces the occurrence at the current position 
    boolean replaced = _machine.replaceCurrent();
    if (replaced)
        _selectFoundOrReplacedItem(replaceWord.length());
    _replaceAction.setEnabled(false);
    _replaceFindNextAction.setEnabled(false);
    _replaceFindPreviousAction.setEnabled(false);
    _replaceButton.requestFocusInWindow();
}

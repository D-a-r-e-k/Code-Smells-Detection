/** Called from MainFrame in response to opening this or changes in the active document. */
void beginListeningTo(DefinitionsPane defPane) {
    if (_defPane == null) {
        // removed so it doesn't give the pane focus when switching documents 
        //      requestFocusInWindow();  
        _displayed = true;
        _defPane = defPane;
        _defPane.addCaretListener(_caretListener);
        _caretChanged = true;
        _updateMachine();
        _machine.setFindWord(_findField.getText());
        _machine.setReplaceWord(_replaceField.getText());
        _frame.clearStatusMessage();
        // _message.setText(""); // JL 
        if (!_machine.onMatch() || _findField.getText().equals("")) {
            _replaceAction.setEnabled(false);
            _replaceFindNextAction.setEnabled(false);
            _replaceFindPreviousAction.setEnabled(false);
        } else {
            _replaceAction.setEnabled(true);
            _replaceFindNextAction.setEnabled(true);
            _replaceFindPreviousAction.setEnabled(true);
            _machine.setLastFindWord();
        }
        if (_findField.getText().equals(""))
            _replaceAllAction.setEnabled(false);
        else
            _replaceAllAction.setEnabled(true);
        _frame.clearStatusMessage();
    } else
        throw new UnexpectedException(new RuntimeException("FindReplacePanel should not be listening to anything"));
}

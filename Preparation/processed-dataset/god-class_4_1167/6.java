/** Performs the "replace all" command. */
private void _replaceAll() {
    _frame.updateStatusField("Replacing All");
    //    _updateMachine(); 
    _machine.setFindWord(_findField.getText());
    _machine.setReplaceWord(_replaceField.getText());
    _machine.setSearchBackwards(false);
    OpenDefinitionsDocument startDoc = _defPane.getOpenDefDocument();
    MovingDocumentRegion region = new MovingDocumentRegion(startDoc, _defPane.getSelectionStart(), _defPane.getSelectionEnd(), startDoc._getLineStartPos(_defPane.getSelectionStart()), startDoc._getLineEndPos(_defPane.getSelectionEnd()));
    _machine.setSelection(region);
    _frame.clearStatusMessage();
    int count = _machine.replaceAll();
    Toolkit.getDefaultToolkit().beep();
    _frame.setStatusMessage("Replaced " + count + " occurrence" + ((count == 1) ? "" : "s") + ".");
    _replaceAction.setEnabled(false);
    _replaceFindNextAction.setEnabled(false);
    _replaceFindPreviousAction.setEnabled(false);
    _model.refreshActiveDocument();
}

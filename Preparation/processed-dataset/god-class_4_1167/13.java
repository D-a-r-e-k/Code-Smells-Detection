/** Called from MainFrame upon closing this Dialog or changes in the active document. */
public void stopListening() {
    if (_defPane != null) {
        _defPane.removeCaretListener(_caretListener);
        _defPane = null;
        _displayed = false;
        _frame.clearStatusMessage();
    }
}

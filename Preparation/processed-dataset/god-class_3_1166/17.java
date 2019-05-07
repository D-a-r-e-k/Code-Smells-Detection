/** Resets the modification state of this document.  Used after a document has been saved or reverted. */
public void resetModification() {
    _isModifiedSinceSave = false;
    _undoManager.documentSaved();
    if (_odd != null)
        _odd.documentReset();
}

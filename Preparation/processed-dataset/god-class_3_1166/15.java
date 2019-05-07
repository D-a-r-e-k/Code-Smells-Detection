/** Resets the modification state of this document to be consistent with state of _undoManager.  Called whenever
    * an undo or redo is performed. */
public void updateModifiedSinceSave() {
    _isModifiedSinceSave = _undoManager.isModified();
    if (_odd != null)
        _odd.documentReset();
}

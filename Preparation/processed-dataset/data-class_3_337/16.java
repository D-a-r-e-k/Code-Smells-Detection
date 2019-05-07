/** Sets the modification state of this document to true and updates the state of the associated _odd. 
    * Assumes that write lock is already held. 
    */
private void _setModifiedSinceSave() {
    /* */
    assert Utilities.TEST_MODE || EventQueue.isDispatchThread();
    if (!_isModifiedSinceSave) {
        _isModifiedSinceSave = true;
        if (_odd != null)
            _odd.documentModified();
    }
}

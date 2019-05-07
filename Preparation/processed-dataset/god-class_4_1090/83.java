//}}}  
//{{{ beginCompoundEdit() method  
/**
	 * Starts a compound edit. All edits from now on until
	 * {@link #endCompoundEdit()} are called will be merged
	 * into one. This can be used to make a complex operation
	 * undoable in one step. Nested calls to
	 * {@link #beginCompoundEdit()} behave as expected,
	 * requiring the same number of {@link #endCompoundEdit()}
	 * calls to end the edit.
	 * @see #endCompoundEdit()
	 */
public void beginCompoundEdit() {
    try {
        writeLock();
        undoMgr.beginCompoundEdit();
    } finally {
        writeUnlock();
    }
}

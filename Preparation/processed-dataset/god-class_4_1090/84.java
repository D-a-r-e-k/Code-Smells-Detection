//}}}  
//{{{ endCompoundEdit() method  
/**
	 * Ends a compound edit. All edits performed since
	 * {@link #beginCompoundEdit()} was called can now
	 * be undone in one step by calling {@link #undo(TextArea)}.
	 * @see #beginCompoundEdit()
	 */
public void endCompoundEdit() {
    try {
        writeLock();
        undoMgr.endCompoundEdit();
        if (!insideCompoundEdit())
            fireTransactionComplete();
    } finally {
        writeUnlock();
    }
}

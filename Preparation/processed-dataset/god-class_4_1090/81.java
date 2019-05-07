//}}}  
//{{{ redo() method  
/**
	 * Redoes the most recently undone edit.
	 *
	 * @since jEdit 2.7pre2
	 */
public void redo(TextArea textArea) {
    if (undoMgr == null)
        return;
    if (!isEditable()) {
        Toolkit.getDefaultToolkit().beep();
        return;
    }
    try {
        writeLock();
        undoInProgress = true;
        fireBeginRedo();
        int caret = undoMgr.redo();
        if (caret == -1)
            textArea.getToolkit().beep();
        else
            textArea.setCaretPosition(caret);
        fireEndRedo();
        fireTransactionComplete();
    } finally {
        undoInProgress = false;
        writeUnlock();
    }
}

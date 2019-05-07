//}}}  
//}}}  
//{{{ Undo  
//{{{ undo() method  
/**
	 * Undoes the most recent edit.
	 *
	 * @since jEdit 4.0pre1
	 */
public void undo(TextArea textArea) {
    if (undoMgr == null)
        return;
    if (!isEditable()) {
        textArea.getToolkit().beep();
        return;
    }
    try {
        writeLock();
        undoInProgress = true;
        fireBeginUndo();
        int caret = undoMgr.undo();
        if (caret == -1)
            textArea.getToolkit().beep();
        else
            textArea.setCaretPosition(caret);
        fireEndUndo();
        fireTransactionComplete();
    } finally {
        undoInProgress = false;
        writeUnlock();
    }
}

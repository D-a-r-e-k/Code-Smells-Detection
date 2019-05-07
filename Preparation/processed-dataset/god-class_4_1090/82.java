//}}}  
//{{{ isTransactionInProgress() method  
/**
	 * Returns if an undo or compound edit is currently in progress. If this
	 * method returns true, then eventually a
	 * {@link org.gjt.sp.jedit.buffer.BufferListener#transactionComplete(JEditBuffer)}
	 * buffer event will get fired.
	 * @since jEdit 4.0pre6
	 */
public boolean isTransactionInProgress() {
    return transaction || undoInProgress || insideCompoundEdit();
}

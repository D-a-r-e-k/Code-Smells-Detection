//}}}  
//{{{ getUndoId() method  
/**
	 * Returns an object that identifies the undo operation to which the
	 * current content change belongs. This method can be used by buffer
	 * listeners during content changes (contentInserted/contentRemoved)
	 * to find out which content changes belong to the same "undo" operation.
	 * The same undoId object will be returned for all content changes
	 * belonging to the same undo operation. Only the identity of the
	 * undoId can be used, by comparing it with a previously-returned undoId
	 * using "==".
	 * @since jEdit 4.3pre18
	 */
public Object getUndoId() {
    return undoMgr.getUndoId();
}

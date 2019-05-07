//}}}  
//{{{ setUndoLimit() method  
/**
	 * Set the undo limit of the Undo Manager.
	 *
	 * @param limit the new limit
	 * @since jEdit 4.3pre16
	 */
public void setUndoLimit(int limit) {
    if (undoMgr != null)
        undoMgr.setLimit(limit);
}

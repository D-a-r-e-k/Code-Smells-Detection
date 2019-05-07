//}}}  
//{{{ canUndo() method  
/**
	 * Returns true if an undo operation can be performed.
	 * @since jEdit 4.3pre18
	 */
public boolean canUndo() {
    if (undoMgr == null)
        return false;
    return undoMgr.canUndo();
}

//}}}  
//{{{ canRedo() method  
/**
	 * Returns true if a redo operation can be performed.
	 * @since jEdit 4.3pre18
	 */
public boolean canRedo() {
    if (undoMgr == null)
        return false;
    return undoMgr.canRedo();
}

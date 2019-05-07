//}}}  
//{{{ insideCompoundEdit() method  
/**
	 * Returns if a compound edit is currently active.
	 * @since jEdit 3.1pre1
	 */
public boolean insideCompoundEdit() {
    return undoMgr.insideCompoundEdit();
}

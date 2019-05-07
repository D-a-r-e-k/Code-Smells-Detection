//}}}  
//{{{ getVFSFileFilter() method  
/**
	 * Returns the currently active VFSFileFilter.
	 *
	 * @since jEdit 4.3pre7
	 */
public VFSFileFilter getVFSFileFilter() {
    if (mode == CHOOSE_DIRECTORY_DIALOG)
        return new DirectoriesOnlyFilter();
    return (VFSFileFilter) filterField.getSelectedItem();
}

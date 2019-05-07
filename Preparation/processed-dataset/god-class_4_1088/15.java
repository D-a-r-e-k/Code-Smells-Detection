//}}}  
//{{{ getFilenameFilter() method  
/**
	 * Returns the file name filter glob.
	 * @since jEdit 3.2pre2
	 * @deprecated Use {@link #getVFSFileFilter()} instead. This method
	 *             might return wrong information since jEdit 4.3pre6.
	 */
@Deprecated
public String getFilenameFilter() {
    if (filterCheckbox.isSelected()) {
        String filter = filterField.getSelectedItem().toString();
        if (filter.length() == 0)
            return "*";
        else
            return filter;
    } else
        return "*";
}

//}}}  
//{{{ setFilenameFilter() method  
public void setFilenameFilter(String filter) {
    if (filter == null || filter.length() == 0 || "*".equals(filter))
        filterCheckbox.setSelected(false);
    else {
        filterCheckbox.setSelected(true);
        filterEditor.setItem(new GlobVFSFileFilter(filter));
    }
}

//}}}  
//{{{ removeNotify() method  
@Override
public void removeNotify() {
    super.removeNotify();
    jEdit.setBooleanProperty("vfs.browser.filter-enabled", filterCheckbox.isSelected());
    if (mode == BROWSER || !jEdit.getBooleanProperty("vfs.browser.currentBufferFilter")) {
        VFSFileFilter selectedFilter = (VFSFileFilter) filterField.getSelectedItem();
        if (selectedFilter instanceof GlobVFSFileFilter)
            jEdit.setProperty("vfs.browser.last-filter", ((GlobVFSFileFilter) selectedFilter).getGlob());
    }
    EditBus.removeFromBus(this);
}

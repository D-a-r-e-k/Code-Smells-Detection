//}}}  
//{{{ addVFSFileFilter() method  
/**
	 * Adds a file filter to the browser.
	 *
	 * @since jEdit 4.3pre7
	 */
public void addVFSFileFilter(VFSFileFilter filter) {
    filterField.addItem(filter);
    if (filterField.getItemCount() == 2) {
        filterField.setEditor(filterEditor);
        filterField.setEditable(true);
        GridBagLayout layout = (GridBagLayout) pathAndFilterPanel.getLayout();
        GridBagConstraints cons = layout.getConstraints(filterEditor);
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.gridx = 1;
        cons.weightx = 1;
        pathAndFilterPanel.remove(filterEditor);
        layout.setConstraints(filterField, cons);
        pathAndFilterPanel.add(filterField);
        pathAndFilterPanel.validate();
        pathAndFilterPanel.repaint();
    }
}

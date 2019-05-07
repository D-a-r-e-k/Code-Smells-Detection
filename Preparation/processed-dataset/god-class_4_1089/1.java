//}}}  
//{{{ Input/output methods  
//{{{ reload() method  
/**
	 * Reloads the buffer from disk, asking for confirmation if the buffer
	 * has unsaved changes.
	 * @param view The view
	 * @since jEdit 2.7pre2
	 */
public void reload(View view) {
    if (getFlag(UNTITLED))
        return;
    if (isDirty()) {
        String[] args = { path };
        int result = GUIUtilities.confirm(view, "changedreload", args, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result != JOptionPane.YES_OPTION)
            return;
    }
    view.visit(new SaveCaretInfoVisitor());
    load(view, true);
}

//}}}  
//{{{ getIcon() method  
/**
	 * Returns this buffer's icon.
	 * @since jEdit 2.6pre6
	 */
public Icon getIcon() {
    if (isDirty())
        return GUIUtilities.loadIcon("dirty.gif");
    else if (isReadOnly())
        return GUIUtilities.loadIcon("readonly.gif");
    else if (getFlag(NEW_FILE))
        return GUIUtilities.loadIcon("new.gif");
    else
        return GUIUtilities.loadIcon("normal.gif");
}

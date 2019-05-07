//}}}  
//{{{ newFile() method  
/**
	 * Creates a new file in the current directory.
	 * @since jEdit 4.0pre2
	 */
public void newFile() {
    VFSFile[] selected = getSelectedFiles();
    if (selected.length >= 1) {
        VFSFile file = selected[0];
        if (file.getType() == VFSFile.DIRECTORY)
            jEdit.newFile(view, file.getPath());
        else {
            VFS vfs = VFSManager.getVFSForPath(file.getPath());
            jEdit.newFile(view, vfs.getParentOfPath(file.getPath()));
        }
    } else
        jEdit.newFile(view, path);
}

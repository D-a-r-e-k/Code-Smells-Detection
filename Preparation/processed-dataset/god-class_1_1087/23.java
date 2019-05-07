//}}}  
//{{{ _listDirectory() method  
/**
	 * @deprecated Use <code>_listFiles()</code> instead.
	 */
@Deprecated
public DirectoryEntry[] _listDirectory(Object session, String directory, Component comp) throws IOException {
    VFSManager.error(comp, directory, "vfs.not-supported.list", new String[] { name });
    return null;
}

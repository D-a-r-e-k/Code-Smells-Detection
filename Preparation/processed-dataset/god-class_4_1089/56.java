//}}}  
//{{{ getMarkersPath() method  
/**
	 * Returns the path for this buffer's markers file
	 * @param vfs The appropriate VFS
	 * @since jEdit 4.3pre7
	 * @deprecated it will fail if you save to another VFS. use {@link #getMarkersPath(VFS, String)}
	 */
@Deprecated
public String getMarkersPath(VFS vfs) {
    return getMarkersPath(vfs, path);
}

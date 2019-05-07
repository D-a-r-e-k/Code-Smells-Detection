//}}}  
//{{{ getMarkersPath() method  
/**
	 * Returns the path for this buffer's markers file
	 * @param vfs The appropriate VFS
	 * @param path the path of the buffer, it can be different from the field
	 * when using save-as
	 * @since jEdit 4.3pre10
	 */
public static String getMarkersPath(VFS vfs, String path) {
    return vfs.getParentOfPath(path) + '.' + vfs.getFileName(path) + ".marks";
}

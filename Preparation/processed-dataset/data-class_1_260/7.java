//}}}  
//{{{ getParentOfPath() method  
/**
	 * Returns the parent of the specified path. This must be
	 * overridden to return a non-null value for browsing of this
	 * filesystem to work.
	 * @param path The path
	 * @since jEdit 2.6pre5
	 */
public String getParentOfPath(String path) {
    // ignore last character of path to properly handle  
    // paths like /foo/bar/  
    int lastIndex = path.length() - 1;
    while (lastIndex > 0 && (path.charAt(lastIndex) == File.separatorChar || path.charAt(lastIndex) == '/')) {
        lastIndex--;
    }
    int count = Math.max(0, lastIndex);
    int index = path.lastIndexOf(File.separatorChar, count);
    if (index == -1)
        index = path.lastIndexOf('/', count);
    if (index == -1) {
        // this ensures that getFileParent("protocol:"), for  
        // example, is "protocol:" and not "".  
        index = path.lastIndexOf(':');
    }
    return path.substring(0, index + 1);
}

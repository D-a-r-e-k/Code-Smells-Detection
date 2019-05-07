//}}}  
//{{{ getFileName() method  
/**
	 * Returns the file name component of the specified path.
	 * @param path The path
	 * @since jEdit 3.1pre4
	 */
public String getFileName(String path) {
    if (path.equals("/"))
        return path;
    while (path.endsWith("/") || path.endsWith(File.separator)) path = path.substring(0, path.length() - 1);
    int index = Math.max(path.lastIndexOf('/'), path.lastIndexOf(File.separatorChar));
    if (index == -1)
        index = path.indexOf(':');
    // don't want getFileName("roots:") to return ""  
    if (index == -1 || index == path.length() - 1)
        return path;
    return path.substring(index + 1);
}

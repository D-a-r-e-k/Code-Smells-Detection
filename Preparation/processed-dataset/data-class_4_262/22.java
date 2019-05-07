//}}}  
//{{{ getSymlinkPath() method  
/**
	 * If this file is a symbolic link, returns the link destination.
	 * Otherwise returns the file's path. This method is thread-safe.
	 * @since jEdit 4.2pre1
	 */
public String getSymlinkPath() {
    return symlinkPath;
}

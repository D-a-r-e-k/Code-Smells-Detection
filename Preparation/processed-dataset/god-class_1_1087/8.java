//}}}  
//{{{ constructPath() method  
/**
	 * Constructs a path from the specified directory and
	 * file name component. This must be overridden to return a
	 * non-null value, otherwise browsing this filesystem will
	 * not work.<p>
	 *
	 * Unless you are writing a VFS, this method should not be called
	 * directly. To ensure correct behavior, you <b>must</b> call
	 * {@link org.gjt.sp.jedit.MiscUtilities#constructPath(String,String)}
	 * instead.
	 *
	 * @param parent The parent directory
	 * @param path The path
	 * @since jEdit 2.6pre2
	 */
public String constructPath(String parent, String path) {
    return parent + path;
}

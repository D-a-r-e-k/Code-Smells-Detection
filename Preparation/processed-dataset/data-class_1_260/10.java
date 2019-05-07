//}}}  
//{{{ getTwoStageSaveName() method  
/**
	 * Returns a temporary file name based on the given path.
	 *
	 * By default jEdit first saves a file to <code>#<i>name</i>#save#</code>
	 * and then renames it to the original file. However some virtual file
	 * systems might not support the <code>#</code> character in filenames,
	 * so this method permits the VFS to override this behavior.
	 *
	 * If this method returns <code>null</code>, two stage save will not
	 * be used for that particular file (introduced in jEdit 4.3pre1).
	 *
	 * @param path The path name
	 * @since jEdit 4.1pre7
	 */
public String getTwoStageSaveName(String path) {
    return MiscUtilities.constructPath(getParentOfPath(path), '#' + getFileName(path) + "#save#");
}

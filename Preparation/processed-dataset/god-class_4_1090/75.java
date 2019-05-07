//}}}  
//{{{ invalidateCachedFoldLevels() method  
/**
	 * Invalidates all cached fold level information.
	 * @since jEdit 4.1pre11
	 */
public void invalidateCachedFoldLevels() {
    lineMgr.setFirstInvalidFoldLevel(0);
    fireFoldLevelChanged(0, getLineCount());
}

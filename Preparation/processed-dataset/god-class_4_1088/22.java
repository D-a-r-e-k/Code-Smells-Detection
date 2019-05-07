/**
	 * @since jEdit 4.3pre15
	 */
public void nextDirectory() {
    if (!nextDirectoryStack.isEmpty()) {
        setDirectory(nextDirectoryStack.pop());
    }
}

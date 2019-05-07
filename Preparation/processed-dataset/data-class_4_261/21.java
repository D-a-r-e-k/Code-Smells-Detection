//}}}  
// {{{ Directory Stack operations  
/**
	 * @since jedit 4.3pre15
	 */
public void previousDirectory() {
    if (historyStack.size() > 1) {
        historyStack.pop();
        nextDirectoryStack.push(path);
        setDirectory(historyStack.peek());
        historyStack.pop();
    }
}

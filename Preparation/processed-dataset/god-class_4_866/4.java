// evaluateInputSource(XMLInputSource) 
/**
     * Cleans up used resources. For example, if scanning is terminated
     * early, then this method ensures all remaining open streams are
     * closed.
     *
     * @param closeall Close all streams, including the original.
     *                 This is used in cases when the application has
     *                 opened the original document stream and should
     *                 be responsible for closing it.
     */
public void cleanup(boolean closeall) {
    int size = fCurrentEntityStack.size();
    if (size > 0) {
        // current entity is not the original, so close it 
        if (fCurrentEntity != null) {
            fCurrentEntity.closeQuietly();
        }
        // close remaining streams 
        for (int i = closeall ? 0 : 1; i < size; i++) {
            fCurrentEntity = (CurrentEntity) fCurrentEntityStack.pop();
            fCurrentEntity.closeQuietly();
        }
    } else if (closeall && fCurrentEntity != null) {
        fCurrentEntity.closeQuietly();
    }
}

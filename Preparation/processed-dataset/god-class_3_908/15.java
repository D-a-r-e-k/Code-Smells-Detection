//}}} 
//{{{ closeDocumentBuffer() 
/**
     * Closes an open DocumentBuffer. If dirty then the user will be prompted to save.
     * @param view The view that contains the buffer.
     * @param buffer The buffer to close.
     * @return true if the buffer was closed successfully. May return false if
     *         user hits cancel when asked to save changes.
     * @throws IOException if the user chooses to save and the file cannot be saved
     *                     because of an I/O error.
     */
public static boolean closeDocumentBuffer(TabbedView view, DocumentBuffer buffer) throws IOException {
    return closeDocumentBuffer(view, buffer, true);
}

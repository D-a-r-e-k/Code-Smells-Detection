//}}} 
//{{{ openXMLDocument() 
/**
     * Attempts to open an XML document in jsXe from a file on disk. If the file
     * is already open then the view's focus is set to that document.
     * @param view The view to open the document in.
     * @param file The file to open.
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
public static boolean openXMLDocument(TabbedView view, File file) throws IOException {
    /*
        Check if it's in the recent file history
        if so then use those properties
        */
    BufferHistory.BufferHistoryEntry entry = m_bufferHistory.getEntry(file.getPath());
    if (entry != null) {
        return openXMLDocument(view, file, entry.getProperties(), entry.getViewName());
    } else {
        return openXMLDocument(view, file, new Properties(), null);
    }
}

//}}} 
//{{{ openXMLDocument() 
/**
     * Attempts to open an XML document in the form of a Reader object as an
     * untitled document.
     * @param view The view to open the document in.
     * @param stream The stream to the document.
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
public static boolean openXMLDocument(TabbedView view, InputStream stream) throws IOException {
    Log.log(Log.NOTICE, jsXe.class, "Loading Untitled Document");
    DocumentBuffer buffer = new DocumentBuffer(stream);
    try {
        m_buffers.add(buffer);
        view.addDocumentBuffer(buffer);
        return true;
    } catch (IOException ioe) {
        //recover by removing the document 
        m_buffers.remove(buffer);
        throw ioe;
    }
}

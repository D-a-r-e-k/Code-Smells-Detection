//}}} 
//{{{ openXMLDocument() 
/**
     * Attempts to open an XML document in jsXe from a file on disk. If the file
     * is already open then the view's focus is set to that document. The
     * properties and view name given are ignored if the document is already
     * open. If the view name is null then the document is opened in the default
     * DocumentView.
     *
     * @param view The view to open the document in.
     * @param file The file to open.
     * @param properties the properties to set to the new document
     * @param viewName the name of the view to open this document in
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
public static boolean openXMLDocument(TabbedView view, File file, Properties properties, String viewName) throws IOException {
    if (file == null)
        return false;
    DocumentBuffer buffer = getOpenBuffer(file);
    if (buffer != null) {
        /*
            ignore properties since the file is already open and the user may have
            set propeties him/herself. We don't want to change what the user
            has set.
            */
        view.setDocumentBuffer(buffer);
        return true;
    } else {
        Log.log(Log.NOTICE, jsXe.class, "Loading file " + file.getName());
        try {
            buffer = new DocumentBuffer(file, properties);
            m_buffers.add(buffer);
            if (viewName != null) {
                try {
                    view.addDocumentBuffer(buffer, viewName);
                } catch (IOException ioe) {
                    /*
                        we can't open in the view we want try to open in
                        all views
                        */
                    view.addDocumentBuffer(buffer);
                } catch (UnrecognizedPluginException e) {
                    /*
                        we can't open in the view we want try to open in
                        all views
                        */
                    view.addDocumentBuffer(buffer);
                }
            } else {
                view.addDocumentBuffer(buffer);
            }
            /*
                if there was only one untitled, clean buffer open then go
                ahead and close it so it doesn't clutter up the user's
                workspace.
                */
            DocumentBuffer[] buffers = getDocumentBuffers();
            if (buffers.length == 2 && buffers[0].isUntitled() && !buffers[0].getStatus(DocumentBuffer.DIRTY)) {
                closeDocumentBuffer(view, buffers[0]);
            }
            return true;
        } catch (IOException ioe) {
            m_buffers.remove(buffer);
            throw ioe;
        }
    }
}

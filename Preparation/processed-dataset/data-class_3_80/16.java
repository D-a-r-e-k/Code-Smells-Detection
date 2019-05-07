//}}} 
//{{{ closeDocumentBuffer() 
/**
     * Overloaded version of closeDocumentBuffer() method. Checks to see if confirmClose is 
     * is true. If confirmClose is set to true and the file is dirty then the user 
     * will be prompted to confirm that they want to save the file and then they will be 
     * provided with a JFileChooser. If a file is dirty and confirmClose is set to false,
     * the user will be sent directly to the JFileChooser, they won't be asked first
     * if they want to save the file or not.
     * @param view The view that contains the buffer.
     * @param buffer The buffer to close.
     * @param confirmClose Whether or not user should be asked to confirm that they want to save file,
     *        before being sent to JFileChooser.
     * @return true if the buffer was closed successfully. May return false if
     *         user hits cancel when asked to save changes.
     * @throws IOException if the user chooses to save and the file cannot be saved
     *                     because of an I/O error.
     */
public static boolean closeDocumentBuffer(TabbedView view, DocumentBuffer buffer, boolean confirmClose) throws IOException {
    if (m_buffers.contains(buffer)) {
        if (buffer.close(view, confirmClose)) {
            Log.log(Log.NOTICE, jsXe.class, "Closing " + buffer.getName());
            m_bufferHistory.setEntry(buffer, getPluginLoader().getPluginProperty(view.getDocumentView().getViewPlugin(), JARClassLoader.PLUGIN_NAME));
            view.removeDocumentBuffer(buffer);
            m_buffers.remove(buffer);
            if (view.getBufferCount() == 0) {
                if (!m_exiting) {
                    try {
                        openXMLDocument(view, getDefaultDocument());
                    } catch (IOException ioe) {
                        exiterror(view, "Could not open default document.", 1);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}

//}}} 
//{{{ showOpenFileDialog() 
/**
     * Shows an open file dialog for jsXe. When a file is selected jsXe attempts
     * to open it.
     * @param view The view that is to be the parent of the file dialog
     * @return true if the file is selected and opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
public static boolean showOpenFileDialog(TabbedView view) throws IOException {
    DocumentBuffer buffer = view.getDocumentBuffer();
    File docFile = buffer.getFile();
    JFileChooser loadDialog;
    if (docFile == null) {
        loadDialog = new jsxeFileDialog(m_homeDirectory);
    } else {
        loadDialog = new jsxeFileDialog(docFile);
    }
    loadDialog.setMultiSelectionEnabled(true);
    int returnVal = loadDialog.showOpenDialog(view);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        boolean success = false;
        File[] files = loadDialog.getSelectedFiles();
        for (int i = 0; i < files.length; i++) {
            //success becomes true if at least one document is opened 
            //successfully. 
            if (files[i] != null) {
                try {
                    success = openXMLDocument(view, files[i]) || success;
                } catch (IOException ioe) {
                    //I/O error doesn't change value of success 
                    Log.log(Log.WARNING, jsXe.class, ioe);
                    JOptionPane.showMessageDialog(view, ioe, Messages.getMessage("IO.Error.Title"), JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        return success;
    }
    return false;
}

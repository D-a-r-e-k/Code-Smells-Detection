//}}} 
//{{{ closeAllDocumentBuffers() 
/**
     * Closes all open DocumentBuffers. If there are any dirty files then the
     * user will be prompted to save or close them in a single dialog.
     * @param view the view that initiated the close
     * @throws IOException if the user chooses to save and the file cannot
     *                     be saved because of an I/O error
     */
public static boolean closeAllDocumentBuffers(TabbedView view) throws IOException {
    DocumentBuffer[] buffers = jsXe.getDocumentBuffers();
    ArrayList dirtyBufferList = new ArrayList();
    //sequentially close all the document views 
    for (int i = 0; i < buffers.length; i++) {
        DocumentBuffer db = buffers[i];
        if (db.getStatus(DocumentBuffer.DIRTY)) {
            dirtyBufferList.add(db);
        }
    }
    boolean closeFiles = true;
    //produce Dialog box with the list of currently opened files in it 
    if (dirtyBufferList.size() > 0) {
        if (dirtyBufferList.size() > 1) {
            /*
                If there are multiple dirty files then show
                the dirty files dialog
                */
            DirtyFilesDialog dirtyDialog = new DirtyFilesDialog(view, dirtyBufferList);
            dirtyDialog.setSize(200, 400);
            dirtyDialog.setResizable(true);
            closeFiles = !dirtyDialog.getCancelFlag();
        } else {
            /*
                if there is only one file then close the one file normally
                then close the rest of the clean buffers
                */
            closeFiles = closeDocumentBuffer(view, (DocumentBuffer) dirtyBufferList.get(0), true);
        }
    }
    if (closeFiles) {
        //get the buffers that are still open and close them. 
        buffers = jsXe.getDocumentBuffers();
        for (int i = 0; i < buffers.length; i++) {
            if (!closeDocumentBuffer(view, buffers[i], false)) {
                return false;
            }
        }
    }
    return closeFiles;
}

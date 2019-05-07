//}}} 
// {{{ Private static members 
//{{{ openXMLDocuments() 
/**
     * Open the XML documents in the command line arguments.
     */
private static boolean openXMLDocuments(TabbedView view, String args[]) {
    boolean success = false;
    for (int i = 0; i < args.length; i++) {
        //success becomes true if at least one document is opened 
        //successfully. 
        if (args[i] != null) {
            try {
                Log.log(Log.NOTICE, jsXe.class, "Trying to open " + args[i]);
                success = openXMLDocument(view, new File(args[i])) || success;
            } catch (IOException ioe) {
                //I/O error doesn't change value of success 
                Log.log(Log.WARNING, jsXe.class, ioe);
                JOptionPane.showMessageDialog(view, ioe, Messages.getMessage("IO.Error.Title"), JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    return success;
}

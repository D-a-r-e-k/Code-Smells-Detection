//}}} 
//{{{ openXMLDocument() 
/**
     * Attempts to open an XML document in the form of a String object as an
     * untitled document.
     * @param view The view to open the document in.
     * @param doc The String document to open.
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
public static boolean openXMLDocument(TabbedView view, String doc) throws IOException {
    return openXMLDocument(view, new ByteArrayInputStream(doc.getBytes("UTF-8")));
}

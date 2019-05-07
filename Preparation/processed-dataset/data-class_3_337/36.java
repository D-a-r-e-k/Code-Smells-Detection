/** Gets the package name embedded in the text of this document by minimally parsing the document to find the
    * package statement.  If package statement is not found or is ill-formed, returns "" as the package name.
    * @return the name of package embedded in this document.  If there is no well-formed package statement, 
    * returns "" as the package name.
    */
public String getPackageName() {
    // assert EventQueue.isDispatchThread(); 
    Reader r;
    r = new StringReader(getText());
    // getText() is cheap if document is not resident 
    try {
        return new Parser(r).packageDeclaration(Parser.DeclType.TOP).getName();
    } catch (ParseException e) {
        return "";
    } catch (TokenMgrError e) {
        return "";
    } catch (Error e) {
        // JavaCharStream does not use a useful exception type for escape character errors 
        String msg = e.getMessage();
        if (msg != null && msg.startsWith("Invalid escape character")) {
            return "";
        } else {
            throw e;
        }
    } finally {
        try {
            r.close();
        } catch (IOException e) {
        }
    }
}

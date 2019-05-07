// <init>()  
//  
// XMLDocumentScanner methods  
//  
/** 
     * Sets the input source. 
     *
     * @param inputSource The input source.
     *
     * @throws IOException Thrown on i/o error.
     */
public void setInputSource(XMLInputSource inputSource) throws IOException {
    fEntityManager.setEntityHandler(this);
    fEntityManager.startEntity("$fragment$", inputSource, false, true);
}

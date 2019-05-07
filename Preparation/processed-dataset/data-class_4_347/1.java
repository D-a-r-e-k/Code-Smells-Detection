/**
   * This static method simplifies the creation of an SQL Document and allows
   * us to embedd the complexity of creating / handling the dtmIdent inside
   * the document. This type of method may better placed inside the DTMDocument
   * code
   */
public static SQLDocument getNewDocument(ExpressionContext exprContext) {
    DTMManager mgr = ((XPathContext.XPathExpressionContext) exprContext).getDTMManager();
    DTMManagerDefault mgrDefault = (DTMManagerDefault) mgr;
    int dtmIdent = mgrDefault.getFirstFreeDTMID();
    SQLDocument doc = new SQLDocument(mgr, dtmIdent << DTMManager.IDENT_DTM_NODE_BITS);
    // Register the document  
    mgrDefault.addDTM(doc, dtmIdent);
    doc.setExpressionContext(exprContext);
    return doc;
}

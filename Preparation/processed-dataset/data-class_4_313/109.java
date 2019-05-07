// changeRedefineGroup  
// this method returns the XSDocumentInfo object that contains the  
// component corresponding to decl.  If components from this  
// document cannot be referred to from those of currSchema, this  
// method returns null; it's up to the caller to throw an error.  
// @param:  currSchema:  the XSDocumentInfo object containing the  
// decl ref'ing us.  
// @param:  decl:  the declaration being ref'd.  
// this method is superficial now. ---Jack  
private XSDocumentInfo findXSDocumentForDecl(XSDocumentInfo currSchema, Element decl, XSDocumentInfo decl_Doc) {
    if (DEBUG_NODE_POOL) {
        System.out.println("DOCUMENT NS:" + currSchema.fTargetNamespace + " hashcode:" + ((Object) currSchema.fSchemaElement).hashCode());
    }
    Object temp = decl_Doc;
    if (temp == null) {
        // something went badly wrong; we don't know this doc?  
        return null;
    }
    XSDocumentInfo declDocInfo = (XSDocumentInfo) temp;
    return declDocInfo;
}

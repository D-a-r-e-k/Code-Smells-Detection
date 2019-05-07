// findXSDocumentForDecl(XSDocumentInfo, Element):  XSDocumentInfo  
// returns whether more than <annotation>s occur in children of elem  
private boolean nonAnnotationContent(Element elem) {
    for (Element child = DOMUtil.getFirstChildElement(elem); child != null; child = DOMUtil.getNextSiblingElement(child)) {
        if (!(DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)))
            return true;
    }
    return false;
}

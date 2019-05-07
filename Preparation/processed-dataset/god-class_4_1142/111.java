// nonAnnotationContent(Element):  boolean  
private void setSchemasVisible(XSDocumentInfo startSchema) {
    if (DOMUtil.isHidden(startSchema.fSchemaElement, fHiddenNodes)) {
        // make it visible  
        DOMUtil.setVisible(startSchema.fSchemaElement, fHiddenNodes);
        Vector dependingSchemas = (Vector) fDependencyMap.get(startSchema);
        for (int i = 0; i < dependingSchemas.size(); i++) {
            setSchemasVisible((XSDocumentInfo) dependingSchemas.elementAt(i));
        }
    }
}

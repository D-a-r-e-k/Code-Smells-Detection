// use Schema Element to lookup the SystemId.  
private String doc2SystemId(Element ele) {
    String documentURI = null;
    /**
         * REVISIT: Casting until DOM Level 3 interfaces are available. -- mrglavas
         */
    if (ele.getOwnerDocument() instanceof org.apache.xerces.impl.xs.opti.SchemaDOM) {
        documentURI = ((org.apache.xerces.impl.xs.opti.SchemaDOM) ele.getOwnerDocument()).getDocumentURI();
    }
    return documentURI != null ? documentURI : (String) fDoc2SystemId.get(ele);
}

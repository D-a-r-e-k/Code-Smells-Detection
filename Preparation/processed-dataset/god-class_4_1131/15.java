/**
     * Resolves the specified public and system identifiers. This
     * method first attempts to resolve the entity based on the
     * EntityResolver registered by the application. If no entity
     * resolver is registered or if the registered entity handler
     * is unable to resolve the entity, then default entity
     * resolution will occur.
     *
     * @param resourceIdentifier The XMLResourceIdentifier for the resource to resolve.
     *
     * @return Returns an input source that wraps the resolved entity.
     *         This method will never return null.
     *
     * @throws IOException  Thrown on i/o error.
     * @throws XNIException Thrown by entity resolver to signal an error.
     */
public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws IOException, XNIException {
    if (resourceIdentifier == null)
        return null;
    String publicId = resourceIdentifier.getPublicId();
    String literalSystemId = resourceIdentifier.getLiteralSystemId();
    String baseSystemId = resourceIdentifier.getBaseSystemId();
    String expandedSystemId = resourceIdentifier.getExpandedSystemId();
    // if no base systemId given, assume that it's relative  
    // to the systemId of the current scanned entity  
    // Sometimes the system id is not (properly) expanded.  
    // We need to expand the system id if:  
    // a. the expanded one was null; or  
    // b. the base system id was null, but becomes non-null from the current entity.  
    boolean needExpand = (expandedSystemId == null);
    // REVISIT:  why would the baseSystemId ever be null?  if we  
    // didn't have to make this check we wouldn't have to reuse the  
    // fXMLResourceIdentifier object...  
    if (baseSystemId == null && fCurrentEntity != null && fCurrentEntity.entityLocation != null) {
        baseSystemId = fCurrentEntity.entityLocation.getExpandedSystemId();
        if (baseSystemId != null)
            needExpand = true;
    }
    // give the entity resolver a chance  
    XMLInputSource xmlInputSource = null;
    if (fEntityResolver != null) {
        if (needExpand) {
            expandedSystemId = expandSystemId(literalSystemId, baseSystemId, false);
        }
        resourceIdentifier.setBaseSystemId(baseSystemId);
        resourceIdentifier.setExpandedSystemId(expandedSystemId);
        xmlInputSource = fEntityResolver.resolveEntity(resourceIdentifier);
    }
    // do default resolution  
    // REVISIT: what's the correct behavior if the user provided an entity  
    // resolver (fEntityResolver != null), but resolveEntity doesn't return  
    // an input source (xmlInputSource == null)?  
    // do we do default resolution, or do we just return null? -SG  
    if (xmlInputSource == null) {
        // REVISIT: when systemId is null, I think we should return null.  
        //          is this the right solution? -SG  
        //if (systemId != null)  
        xmlInputSource = new XMLInputSource(publicId, literalSystemId, baseSystemId);
    }
    if (DEBUG_RESOLVER) {
        System.err.println("XMLEntityManager.resolveEntity(" + publicId + ")");
        System.err.println(" = " + xmlInputSource);
    }
    return xmlInputSource;
}

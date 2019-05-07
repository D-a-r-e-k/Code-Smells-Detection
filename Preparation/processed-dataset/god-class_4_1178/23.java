/**
   * The getUnparsedEntityURI function returns the URI of the unparsed
   * entity with the specified name in the same document as the context
   * node (see [3.3 Unparsed Entities]). It returns the empty string if
   * there is no such entity.
   * <p>
   * XML processors may choose to use the System Identifier (if one
   * is provided) to resolve the entity, rather than the URI in the
   * Public Identifier. The details are dependent on the processor, and
   * we would have to support some form of plug-in resolver to handle
   * this properly. Currently, we simply return the System Identifier if
   * present, and hope that it a usable URI or that our caller can
   * map it to one.
   * TODO: Resolve Public Identifiers... or consider changing function name.
   * <p>
   * If we find a relative URI
   * reference, XML expects it to be resolved in terms of the base URI
   * of the document. The DOM doesn't do that for us, and it isn't
   * entirely clear whether that should be done here; currently that's
   * pushed up to a higher level of our application. (Note that DOM Level
   * 1 didn't store the document's base URI.)
   * TODO: Consider resolving Relative URIs.
   * <p>
   * (The DOM's statement that "An XML processor may choose to
   * completely expand entities before the structure model is passed
   * to the DOM" refers only to parsed entities, not unparsed, and hence
   * doesn't affect this function.)
   *
   * @param name A string containing the Entity Name of the unparsed
   * entity.
   *
   * @return String containing the URI of the Unparsed Entity, or an
   * empty string if no such entity exists.
   */
public String getUnparsedEntityURI(String name) {
    String url = "";
    Document doc = (m_root.getNodeType() == Node.DOCUMENT_NODE) ? (Document) m_root : m_root.getOwnerDocument();
    if (null != doc) {
        DocumentType doctype = doc.getDoctype();
        if (null != doctype) {
            NamedNodeMap entities = doctype.getEntities();
            if (null == entities)
                return url;
            Entity entity = (Entity) entities.getNamedItem(name);
            if (null == entity)
                return url;
            String notationName = entity.getNotationName();
            if (null != notationName) // then it's unparsed  
            {
                // The draft says: "The XSLT processor may use the public   
                // identifier to generate a URI for the entity instead of the URI   
                // specified in the system identifier. If the XSLT processor does   
                // not use the public identifier to generate the URI, it must use   
                // the system identifier; if the system identifier is a relative   
                // URI, it must be resolved into an absolute URI using the URI of   
                // the resource containing the entity declaration as the base   
                // URI [RFC2396]."  
                // So I'm falling a bit short here.  
                url = entity.getSystemId();
                if (null == url) {
                    url = entity.getPublicId();
                } else {
                }
            }
        }
    }
    return url;
}

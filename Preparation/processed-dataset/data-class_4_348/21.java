/**
   * Return the public identifier of the external subset,
   * normalized as described in 4.2.2 External Entities [XML]. If there is
   * no external subset or if it has no public identifier, this property
   * has no value.
   *
   * @return the public identifier String object, or null if there is none.
   */
public String getDocumentTypeDeclarationPublicIdentifier() {
    Document doc;
    if (m_root.getNodeType() == Node.DOCUMENT_NODE)
        doc = (Document) m_root;
    else
        doc = m_root.getOwnerDocument();
    if (null != doc) {
        DocumentType dtd = doc.getDoctype();
        if (null != dtd) {
            return dtd.getPublicId();
        }
    }
    return null;
}

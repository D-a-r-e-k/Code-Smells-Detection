/**
   *   A document type declaration information item has the following properties:
   *
   *     1. [system identifier] The system identifier of the external subset, if
   *        it exists. Otherwise this property has no value.
   *
   * @return the system identifier String object, or null if there is none.
   */
public String getDocumentTypeDeclarationSystemIdentifier() {
    Document doc;
    if (m_root.getNodeType() == Node.DOCUMENT_NODE)
        doc = (Document) m_root;
    else
        doc = m_root.getOwnerDocument();
    if (null != doc) {
        DocumentType dtd = doc.getDoctype();
        if (null != dtd) {
            return dtd.getSystemId();
        }
    }
    return null;
}

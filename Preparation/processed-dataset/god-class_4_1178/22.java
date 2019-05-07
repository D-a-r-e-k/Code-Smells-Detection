/**
   * Returns the <code>Element</code> whose <code>ID</code> is given by
   * <code>elementId</code>. If no such element exists, returns
   * <code>DTM.NULL</code>. Behavior is not defined if more than one element
   * has this <code>ID</code>. Attributes (including those
   * with the name "ID") are not of type ID unless so defined by DTD/Schema
   * information available to the DTM implementation.
   * Implementations that do not know whether attributes are of type ID or
   * not are expected to return <code>DTM.NULL</code>.
   *
   * <p>%REVIEW% Presumably IDs are still scoped to a single document,
   * and this operation searches only within a single document, right?
   * Wouldn't want collisions between DTMs in the same process.</p>
   *
   * @param elementId The unique <code>id</code> value for an element.
   * @return The handle of the matching element.
   */
public int getElementById(String elementId) {
    Document doc = (m_root.getNodeType() == Node.DOCUMENT_NODE) ? (Document) m_root : m_root.getOwnerDocument();
    if (null != doc) {
        Node elem = doc.getElementById(elementId);
        if (null != elem) {
            int elemHandle = getHandleFromNode(elem);
            if (DTM.NULL == elemHandle) {
                int identity = m_nodes.size() - 1;
                while (DTM.NULL != (identity = getNextNodeIdentity(identity))) {
                    Node node = getNode(identity);
                    if (node == elem) {
                        elemHandle = getHandleFromNode(elem);
                        break;
                    }
                }
            }
            return elemHandle;
        }
    }
    return DTM.NULL;
}

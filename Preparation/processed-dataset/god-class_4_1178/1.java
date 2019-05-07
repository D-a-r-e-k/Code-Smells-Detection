/**
   * Construct the node map from the node.
   *
   * @param node The node that is to be added to the DTM.
   * @param parentIndex The current parent index.
   * @param previousSibling The previous sibling index.
   * @param forceNodeType If not DTM.NULL, overrides the DOM node type.
   *	Used to force nodes to Text rather than CDATASection when their
   *	coalesced value includes ordinary Text nodes (current DTM behavior).
   *
   * @return The index identity of the node that was added.
   */
protected int addNode(Node node, int parentIndex, int previousSibling, int forceNodeType) {
    int nodeIndex = m_nodes.size();
    // Have we overflowed a DTM Identity's addressing range?  
    if (m_dtmIdent.size() == (nodeIndex >>> DTMManager.IDENT_DTM_NODE_BITS)) {
        try {
            if (m_mgr == null)
                throw new ClassCastException();
            // Handle as Extended Addressing  
            DTMManagerDefault mgrD = (DTMManagerDefault) m_mgr;
            int id = mgrD.getFirstFreeDTMID();
            mgrD.addDTM(this, id, nodeIndex);
            m_dtmIdent.addElement(id << DTMManager.IDENT_DTM_NODE_BITS);
        } catch (ClassCastException e) {
            // %REVIEW% Wrong error message, but I've been told we're trying  
            // not to add messages right not for I18N reasons.  
            // %REVIEW% Should this be a Fatal Error?  
            error(XMLMessages.createXMLMessage(XMLErrorResources.ER_NO_DTMIDS_AVAIL, null));
        }
    }
    m_size++;
    // ensureSize(nodeIndex);  
    int type;
    if (NULL == forceNodeType)
        type = node.getNodeType();
    else
        type = forceNodeType;
    // %REVIEW% The Namespace Spec currently says that Namespaces are  
    // processed in a non-namespace-aware manner, by matching the  
    // QName, even though there is in fact a namespace assigned to  
    // these nodes in the DOM. If and when that changes, we will have  
    // to consider whether we check the namespace-for-namespaces  
    // rather than the node name.  
    //  
    // %TBD% Note that the DOM does not necessarily explicitly declare  
    // all the namespaces it uses. DOM Level 3 will introduce a  
    // namespace-normalization operation which reconciles that, and we  
    // can request that users invoke it or otherwise ensure that the  
    // tree is namespace-well-formed before passing the DOM to Xalan.  
    // But if they don't, what should we do about it? We probably  
    // don't want to alter the source DOM (and may not be able to do  
    // so if it's read-only). The best available answer might be to  
    // synthesize additional DTM Namespace Nodes that don't correspond  
    // to DOM Attr Nodes.  
    if (Node.ATTRIBUTE_NODE == type) {
        String name = node.getNodeName();
        if (name.startsWith("xmlns:") || name.equals("xmlns")) {
            type = DTM.NAMESPACE_NODE;
        }
    }
    m_nodes.addElement(node);
    m_firstch.setElementAt(NOTPROCESSED, nodeIndex);
    m_nextsib.setElementAt(NOTPROCESSED, nodeIndex);
    m_prevsib.setElementAt(previousSibling, nodeIndex);
    m_parent.setElementAt(parentIndex, nodeIndex);
    if (DTM.NULL != parentIndex && type != DTM.ATTRIBUTE_NODE && type != DTM.NAMESPACE_NODE) {
        // If the DTM parent had no children, this becomes its first child.  
        if (NOTPROCESSED == m_firstch.elementAt(parentIndex))
            m_firstch.setElementAt(nodeIndex, parentIndex);
    }
    String nsURI = node.getNamespaceURI();
    // Deal with the difference between Namespace spec and XSLT  
    // definitions of local name. (The former says PIs don't have  
    // localnames; the latter says they do.)  
    String localName = (type == Node.PROCESSING_INSTRUCTION_NODE) ? node.getNodeName() : node.getLocalName();
    // Hack to make DOM1 sort of work...  
    if (((type == Node.ELEMENT_NODE) || (type == Node.ATTRIBUTE_NODE)) && null == localName)
        localName = node.getNodeName();
    // -sb  
    ExpandedNameTable exnt = m_expandedNameTable;
    // %TBD% Nodes created with the old non-namespace-aware DOM  
    // calls createElement() and createAttribute() will never have a  
    // localname. That will cause their expandedNameID to be just the  
    // nodeType... which will keep them from being matched  
    // successfully by name. Since the DOM makes no promise that  
    // those will participate in namespace processing, this is  
    // officially accepted as Not Our Fault. But it might be nice to  
    // issue a diagnostic message!  
    if (node.getLocalName() == null && (type == Node.ELEMENT_NODE || type == Node.ATTRIBUTE_NODE)) {
    }
    int expandedNameID = (null != localName) ? exnt.getExpandedTypeID(nsURI, localName, type) : exnt.getExpandedTypeID(type);
    m_exptype.setElementAt(expandedNameID, nodeIndex);
    indexNode(expandedNameID, nodeIndex);
    if (DTM.NULL != previousSibling)
        m_nextsib.setElementAt(nodeIndex, previousSibling);
    // This should be done after m_exptype has been set, and probably should  
    // always be the last thing we do  
    if (type == DTM.NAMESPACE_NODE)
        declareNamespaceInContext(parentIndex, nodeIndex);
    return nodeIndex;
}

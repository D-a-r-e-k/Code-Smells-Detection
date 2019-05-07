private void verify(Node node, boolean verifyNames, boolean xml11Version) {
    int type = node.getNodeType();
    fLocator.fRelatedNode = node;
    boolean wellformed;
    switch(type) {
        case Node.DOCUMENT_NODE:
            {
                break;
            }
        case Node.DOCUMENT_TYPE_NODE:
            {
                break;
            }
        case Node.ELEMENT_NODE:
            {
                if (verifyNames) {
                    if ((features & NAMESPACES) != 0) {
                        wellformed = CoreDocumentImpl.isValidQName(node.getPrefix(), node.getLocalName(), xml11Version);
                    } else {
                        wellformed = CoreDocumentImpl.isXMLName(node.getNodeName(), xml11Version);
                    }
                    if (!wellformed) {
                        if (fErrorHandler != null) {
                            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "wf-invalid-character-in-node-name", new Object[] { "Element", node.getNodeName() });
                            DOMNormalizer.reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_FATAL_ERROR, "wf-invalid-character-in-node-name");
                        }
                    }
                }
                NamedNodeMap attributes = (node.hasAttributes()) ? node.getAttributes() : null;
                if (attributes != null) {
                    for (int i = 0; i < attributes.getLength(); ++i) {
                        Attr attr = (Attr) attributes.item(i);
                        fLocator.fRelatedNode = attr;
                        DOMNormalizer.isAttrValueWF(fErrorHandler, fError, fLocator, attributes, attr, attr.getValue(), xml11Version);
                        if (verifyNames) {
                            wellformed = CoreDocumentImpl.isXMLName(attr.getNodeName(), xml11Version);
                            if (!wellformed) {
                                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "wf-invalid-character-in-node-name", new Object[] { "Attr", node.getNodeName() });
                                DOMNormalizer.reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_FATAL_ERROR, "wf-invalid-character-in-node-name");
                            }
                        }
                    }
                }
                break;
            }
        case Node.COMMENT_NODE:
            {
                // only verify well-formness if comments included in the tree  
                if ((features & COMMENTS) != 0)
                    DOMNormalizer.isCommentWF(fErrorHandler, fError, fLocator, ((Comment) node).getData(), xml11Version);
                break;
            }
        case Node.ENTITY_REFERENCE_NODE:
            {
                // only if entity is preserved in the tree  
                if (verifyNames && (features & ENTITIES) != 0) {
                    CoreDocumentImpl.isXMLName(node.getNodeName(), xml11Version);
                }
                break;
            }
        case Node.CDATA_SECTION_NODE:
            {
                // verify content  
                DOMNormalizer.isXMLCharWF(fErrorHandler, fError, fLocator, node.getNodeValue(), xml11Version);
                // the ]]> string will be checked during serialization  
                break;
            }
        case Node.TEXT_NODE:
            {
                DOMNormalizer.isXMLCharWF(fErrorHandler, fError, fLocator, node.getNodeValue(), xml11Version);
                break;
            }
        case Node.PROCESSING_INSTRUCTION_NODE:
            {
                ProcessingInstruction pinode = (ProcessingInstruction) node;
                String target = pinode.getTarget();
                if (verifyNames) {
                    if (xml11Version) {
                        wellformed = XML11Char.isXML11ValidName(target);
                    } else {
                        wellformed = XMLChar.isValidName(target);
                    }
                    if (!wellformed) {
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "wf-invalid-character-in-node-name", new Object[] { "Element", node.getNodeName() });
                        DOMNormalizer.reportDOMError(fErrorHandler, fError, fLocator, msg, DOMError.SEVERITY_FATAL_ERROR, "wf-invalid-character-in-node-name");
                    }
                }
                DOMNormalizer.isXMLCharWF(fErrorHandler, fError, fLocator, pinode.getData(), xml11Version);
                break;
            }
    }
    fLocator.fRelatedNode = null;
}

/**
   * Retrieve the text content of a DOM subtree, appending it into a
   * user-supplied FastStringBuffer object. Note that attributes are
   * not considered part of the content of an element.
   * <p>
   * There are open questions regarding whitespace stripping. 
   * Currently we make no special effort in that regard, since the standard
   * DOM doesn't yet provide DTD-based information to distinguish
   * whitespace-in-element-context from genuine #PCDATA. Note that we
   * should probably also consider xml:space if/when we address this.
   * DOM Level 3 may solve the problem for us.
   * <p>
   * %REVIEW% Note that as a DOM-level operation, it can be argued that this
   * routine _shouldn't_ perform any processing beyond what the DOM already
   * does, and that whitespace stripping and so on belong at the DTM level.
   * If you want a stripped DOM view, wrap DTM2DOM around DOM2DTM.
   *
   * @param node Node whose subtree is to be walked, gathering the
   * contents of all Text or CDATASection nodes.
   */
protected static void dispatchNodeData(Node node, org.xml.sax.ContentHandler ch, int depth) throws org.xml.sax.SAXException {
    switch(node.getNodeType()) {
        case Node.DOCUMENT_FRAGMENT_NODE:
        case Node.DOCUMENT_NODE:
        case Node.ELEMENT_NODE:
            {
                for (Node child = node.getFirstChild(); null != child; child = child.getNextSibling()) {
                    dispatchNodeData(child, ch, depth + 1);
                }
            }
            break;
        case Node.PROCESSING_INSTRUCTION_NODE:
        // %REVIEW%  
        case Node.COMMENT_NODE:
            if (0 != depth)
                break;
        // NOTE: Because this operation works in the DOM space, it does _not_ attempt  
        // to perform Text Coalition. That should only be done in DTM space.   
        case Node.TEXT_NODE:
        case Node.CDATA_SECTION_NODE:
        case Node.ATTRIBUTE_NODE:
            String str = node.getNodeValue();
            if (ch instanceof CharacterNodeHandler) {
                ((CharacterNodeHandler) ch).characters(node);
            } else {
                ch.characters(str.toCharArray(), 0, str.length());
            }
            break;
        //    /* case Node.PROCESSING_INSTRUCTION_NODE :  
        //      // warning(XPATHErrorResources.WG_PARSING_AND_PREPARING);          
        //      break; */  
        default:
            // ignore  
            break;
    }
}

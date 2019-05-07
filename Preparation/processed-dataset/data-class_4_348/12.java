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
   * %REVIEW% Actually, since this method operates on the DOM side of the
   * fence rather than the DTM side, it SHOULDN'T do
   * any special handling. The DOM does what the DOM does; if you want
   * DTM-level abstractions, use DTM-level methods.
   *
   * @param node Node whose subtree is to be walked, gathering the
   * contents of all Text or CDATASection nodes.
   * @param buf FastStringBuffer into which the contents of the text
   * nodes are to be concatenated.
   */
protected static void getNodeData(Node node, FastStringBuffer buf) {
    switch(node.getNodeType()) {
        case Node.DOCUMENT_FRAGMENT_NODE:
        case Node.DOCUMENT_NODE:
        case Node.ELEMENT_NODE:
            {
                for (Node child = node.getFirstChild(); null != child; child = child.getNextSibling()) {
                    getNodeData(child, buf);
                }
            }
            break;
        case Node.TEXT_NODE:
        case Node.CDATA_SECTION_NODE:
        case Node.ATTRIBUTE_NODE:
            // Never a child but might be our starting node  
            buf.append(node.getNodeValue());
            break;
        case Node.PROCESSING_INSTRUCTION_NODE:
            // warning(XPATHErrorResources.WG_PARSING_AND_PREPARING);          
            break;
        default:
            // ignore  
            break;
    }
}

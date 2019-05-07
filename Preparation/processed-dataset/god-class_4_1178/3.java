/**
   * This method iterates to the next node that will be added to the table.
   * Each call to this method adds a new node to the table, unless the end
   * is reached, in which case it returns null.
   *
   * @return The true if a next node is found or false if 
   *         there are no more nodes.
   */
protected boolean nextNode() {
    // Non-recursive one-fetch-at-a-time depth-first traversal with   
    // attribute/namespace nodes and white-space stripping.  
    // Navigating the DOM is simple, navigating the DTM is simple;  
    // keeping track of both at once is a trifle baroque but at least  
    // we've avoided most of the special cases.  
    if (m_nodesAreProcessed)
        return false;
    // %REVIEW% Is this local copy Really Useful from a performance  
    // point of view?  Or is this a false microoptimization?  
    Node pos = m_pos;
    Node next = null;
    int nexttype = NULL;
    // Navigate DOM tree  
    do {
        // Look down to first child.  
        if (pos.hasChildNodes()) {
            next = pos.getFirstChild();
            // %REVIEW% There's probably a more elegant way to skip  
            // the doctype. (Just let it go and Suppress it?  
            if (next != null && DOCUMENT_TYPE_NODE == next.getNodeType())
                next = next.getNextSibling();
            // Push DTM context -- except for children of Entity References,   
            // which have no DTM equivalent and cause no DTM navigation.  
            if (ENTITY_REFERENCE_NODE != pos.getNodeType()) {
                m_last_parent = m_last_kid;
                m_last_kid = NULL;
                // Whitespace-handler context stacking  
                if (null != m_wsfilter) {
                    short wsv = m_wsfilter.getShouldStripSpace(makeNodeHandle(m_last_parent), this);
                    boolean shouldStrip = (DTMWSFilter.INHERIT == wsv) ? getShouldStripWhitespace() : (DTMWSFilter.STRIP == wsv);
                    pushShouldStripWhitespace(shouldStrip);
                }
            }
        } else {
            if (m_last_kid != NULL) {
                // Last node posted at this level had no more children  
                // If it has _no_ children, we need to record that.  
                if (m_firstch.elementAt(m_last_kid) == NOTPROCESSED)
                    m_firstch.setElementAt(NULL, m_last_kid);
            }
            while (m_last_parent != NULL) {
                // %REVIEW% There's probably a more elegant way to  
                // skip the doctype. (Just let it go and Suppress it?  
                next = pos.getNextSibling();
                if (next != null && DOCUMENT_TYPE_NODE == next.getNodeType())
                    next = next.getNextSibling();
                if (next != null)
                    break;
                // Found it!  
                // No next-sibling found. Pop the DOM.  
                pos = pos.getParentNode();
                if (pos == null) {
                    // %TBD% Should never arise, but I want to be sure of that...  
                    if (JJK_DEBUG) {
                        System.out.println("***** DOM2DTM Pop Control Flow problem");
                        for (; ; ) ;
                    }
                }
                // The only parents in the DTM are Elements.  However,  
                // the DOM could contain EntityReferences.  If we  
                // encounter one, pop it _without_ popping DTM.  
                if (pos != null && ENTITY_REFERENCE_NODE == pos.getNodeType()) {
                    // Nothing needs doing  
                    if (JJK_DEBUG)
                        System.out.println("***** DOM2DTM popping EntRef");
                } else {
                    popShouldStripWhitespace();
                    // Fix and pop DTM  
                    if (m_last_kid == NULL)
                        m_firstch.setElementAt(NULL, m_last_parent);
                    else
                        m_nextsib.setElementAt(NULL, m_last_kid);
                    // Popping from anything else  
                    m_last_parent = m_parent.elementAt(m_last_kid = m_last_parent);
                }
            }
            if (m_last_parent == NULL)
                next = null;
        }
        if (next != null)
            nexttype = next.getNodeType();
        // If it's an entity ref, advance past it.  
        //  
        // %REVIEW% Should we let this out the door and just suppress it?  
        // More work, but simpler code, more likely to be correct, and  
        // it doesn't happen very often. We'd get rid of the loop too.  
        if (ENTITY_REFERENCE_NODE == nexttype)
            pos = next;
    } while (ENTITY_REFERENCE_NODE == nexttype);
    // Did we run out of the tree?  
    if (next == null) {
        m_nextsib.setElementAt(NULL, 0);
        m_nodesAreProcessed = true;
        m_pos = null;
        if (JJK_DEBUG) {
            System.out.println("***** DOM2DTM Crosscheck:");
            for (int i = 0; i < m_nodes.size(); ++i) System.out.println(i + ":\t" + m_firstch.elementAt(i) + "\t" + m_nextsib.elementAt(i));
        }
        return false;
    }
    // Text needs some special handling:  
    //  
    // DTM may skip whitespace. This is handled by the suppressNode flag, which  
    // when true will keep the DTM node from being created.  
    //  
    // DTM only directly records the first DOM node of any logically-contiguous  
    // sequence. The lastTextNode value will be set to the last node in the   
    // contiguous sequence, and -- AFTER the DTM addNode -- can be used to   
    // advance next over this whole block. Should be simpler than special-casing  
    // the above loop for "Was the logically-preceeding sibling a text node".  
    //   
    // Finally, a DTM node should be considered a CDATASection only if all the  
    // contiguous text it covers is CDATASections. The first Text should  
    // force DTM to Text.  
    boolean suppressNode = false;
    Node lastTextNode = null;
    nexttype = next.getNodeType();
    // nexttype=pos.getNodeType();  
    if (TEXT_NODE == nexttype || CDATA_SECTION_NODE == nexttype) {
        // If filtering, initially assume we're going to suppress the node  
        suppressNode = ((null != m_wsfilter) && getShouldStripWhitespace());
        // Scan logically contiguous text (siblings, plus "flattening"  
        // of entity reference boundaries).  
        Node n = next;
        while (n != null) {
            lastTextNode = n;
            // Any Text node means DTM considers it all Text  
            if (TEXT_NODE == n.getNodeType())
                nexttype = TEXT_NODE;
            // Any non-whitespace in this sequence blocks whitespace  
            // suppression  
            suppressNode &= XMLCharacterRecognizer.isWhiteSpace(n.getNodeValue());
            n = logicalNextDOMTextNode(n);
        }
    } else if (PROCESSING_INSTRUCTION_NODE == nexttype) {
        suppressNode = (pos.getNodeName().toLowerCase().equals("xml"));
    }
    if (!suppressNode) {
        // Inserting next. NOTE that we force the node type; for  
        // coalesced Text, this records CDATASections adjacent to  
        // ordinary Text as Text.  
        int nextindex = addNode(next, m_last_parent, m_last_kid, nexttype);
        m_last_kid = nextindex;
        if (ELEMENT_NODE == nexttype) {
            int attrIndex = NULL;
            // start with no previous sib  
            // Process attributes _now_, rather than waiting.  
            // Simpler control flow, makes NS cache available immediately.  
            NamedNodeMap attrs = next.getAttributes();
            int attrsize = (attrs == null) ? 0 : attrs.getLength();
            if (attrsize > 0) {
                for (int i = 0; i < attrsize; ++i) {
                    // No need to force nodetype in this case;  
                    // addNode() will take care of switching it from  
                    // Attr to Namespace if necessary.  
                    attrIndex = addNode(attrs.item(i), nextindex, attrIndex, NULL);
                    m_firstch.setElementAt(DTM.NULL, attrIndex);
                    // If the xml: prefix is explicitly declared  
                    // we don't need to synthesize one.  
                    //  
                    // NOTE that XML Namespaces were not originally  
                    // defined as being namespace-aware (grrr), and  
                    // while the W3C is planning to fix this it's  
                    // safer for now to test the QName and trust the  
                    // parsers to prevent anyone from redefining the  
                    // reserved xmlns: prefix  
                    if (!m_processedFirstElement && "xmlns:xml".equals(attrs.item(i).getNodeName()))
                        m_processedFirstElement = true;
                }
            }
            // if attrs exist  
            if (!m_processedFirstElement) {
                // The DOM might not have an explicit declaration for the  
                // implicit "xml:" prefix, but the XPath data model  
                // requires that this appear as a Namespace Node so we  
                // have to synthesize one. You can think of this as  
                // being a default attribute defined by the XML  
                // Namespaces spec rather than by the DTD.  
                attrIndex = addNode(new DOM2DTMdefaultNamespaceDeclarationNode((Element) next, "xml", NAMESPACE_DECL_NS, makeNodeHandle(((attrIndex == NULL) ? nextindex : attrIndex) + 1)), nextindex, attrIndex, NULL);
                m_firstch.setElementAt(DTM.NULL, attrIndex);
                m_processedFirstElement = true;
            }
            if (attrIndex != NULL)
                m_nextsib.setElementAt(DTM.NULL, attrIndex);
        }
    }
    // (if !suppressNode)  
    // Text postprocessing: Act on values stored above  
    if (TEXT_NODE == nexttype || CDATA_SECTION_NODE == nexttype) {
        // %TBD% If nexttype was forced to TEXT, patch the DTM node  
        next = lastTextNode;
    }
    // Remember where we left off.  
    m_pos = next;
    return true;
}

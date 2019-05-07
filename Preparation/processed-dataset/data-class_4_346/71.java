/**
     * Adds the given attribute to the set of attributes, even if there is
     * no currently open element. This is useful if a SAX startPrefixMapping()
     * should need to add an attribute before the element name is seen.
     * 
     * This method is a copy of its super classes method, except that some
     * tracing of events is done.  This is so the tracing is only done for
     * stream serializers, not for SAX ones.
     *
     * @param uri the URI of the attribute
     * @param localName the local name of the attribute
     * @param rawName   the qualified name of the attribute
     * @param type the type of the attribute (probably CDATA)
     * @param value the value of the attribute
     * @param xslAttribute true if this attribute is coming from an xsl:attribute element.
     * @return true if the attribute value was added, 
     * false if the attribute already existed and the value was
     * replaced with the new value.
     */
public boolean addAttributeAlways(String uri, String localName, String rawName, String type, String value, boolean xslAttribute) {
    boolean was_added;
    int index;
    if (uri == null || localName == null || uri.length() == 0)
        index = m_attributes.getIndex(rawName);
    else {
        index = m_attributes.getIndex(uri, localName);
    }
    if (index >= 0) {
        String old_value = null;
        if (m_tracer != null) {
            old_value = m_attributes.getValue(index);
            if (value.equals(old_value))
                old_value = null;
        }
        /* We've seen the attribute before.
             * We may have a null uri or localName, but all we really
             * want to re-set is the value anyway.
             */
        m_attributes.setValue(index, value);
        was_added = false;
        if (old_value != null)
            firePseudoAttributes();
    } else {
        // the attribute doesn't exist yet, create it  
        if (xslAttribute) {
            /*
                 * This attribute is from an xsl:attribute element so we take some care in
                 * adding it, e.g.
                 *   <elem1  foo:attr1="1" xmlns:foo="uri1">
                 *       <xsl:attribute name="foo:attr2">2</xsl:attribute>
                 *   </elem1>
                 * 
                 * We are adding attr1 and attr2 both as attributes of elem1,
                 * and this code is adding attr2 (the xsl:attribute ).
                 * We could have a collision with the prefix like in the example above.
                 */
            // In the example above, is there a prefix like foo ?  
            final int colonIndex = rawName.indexOf(':');
            if (colonIndex > 0) {
                String prefix = rawName.substring(0, colonIndex);
                NamespaceMappings.MappingRecord existing_mapping = m_prefixMap.getMappingFromPrefix(prefix);
                /* Before adding this attribute (foo:attr2),
                     * is the prefix for it (foo) already mapped at the current depth?
                     */
                if (existing_mapping != null && existing_mapping.m_declarationDepth == m_elemContext.m_currentElemDepth && !existing_mapping.m_uri.equals(uri)) {
                    /*
                         * There is an existing mapping of this prefix,
                         * it differs from the one we need,
                         * and unfortunately it is at the current depth so we 
                         * can not over-ride it.
                         */
                    /*
                         * Are we lucky enough that an existing other prefix maps to this URI ?
                         */
                    prefix = m_prefixMap.lookupPrefix(uri);
                    if (prefix == null) {
                        /* Unfortunately there is no existing prefix that happens to map to ours,
                             * so to avoid a prefix collision we must generated a new prefix to use. 
                             * This is OK because the prefix URI mapping
                             * defined in the xsl:attribute is short in scope, 
                             * just the xsl:attribute element itself, 
                             * and at this point in serialization the body of the
                             * xsl:attribute, if any, is just a String. Right?
                             *   . . . I sure hope so - Brian M. 
                             */
                        prefix = m_prefixMap.generateNextPrefix();
                    }
                    rawName = prefix + ':' + localName;
                }
            }
            try {
                /* This is our last chance to make sure the namespace for this
                     * attribute is declared, especially if we just generated an alternate
                     * prefix to avoid a collision (the new prefix/rawName will go out of scope
                     * soon and be lost ...  last chance here.
                     */
                String prefixUsed = ensureAttributesNamespaceIsDeclared(uri, localName, rawName);
            } catch (SAXException e) {
                // TODO Auto-generated catch block  
                e.printStackTrace();
            }
        }
        m_attributes.addAttribute(uri, localName, rawName, type, value);
        was_added = true;
        if (m_tracer != null)
            firePseudoAttributes();
    }
    return was_added;
}

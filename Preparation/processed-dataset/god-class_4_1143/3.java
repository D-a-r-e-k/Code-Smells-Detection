/**
     * Check whether the specified element conforms to the attributes restriction
     * an array of attribute values is returned. the caller must call
     * <code>returnAttrArray</code> to return that array. This method also takes
     * an extra parameter: if the element is "enumeration", whether to make a
     * copy of the namespace context, so that the value can be resolved as a
     * QName later.
     *
     * @param element      which element to check
     * @param isGlobal     whether a child of &lt;schema&gt; or &lt;redefine&gt;
     * @param schemaDoc    the document where the element lives in
     * @param enumAsQName  whether to tread enumeration value as QName
     * @return             an array containing attribute values
     */
public Object[] checkAttributes(Element element, boolean isGlobal, XSDocumentInfo schemaDoc, boolean enumAsQName) {
    if (element == null)
        return null;
    // get all attributes  
    Attr[] attrs = DOMUtil.getAttrs(element);
    // update NamespaceSupport  
    resolveNamespace(element, attrs, schemaDoc.fNamespaceSupport);
    String uri = DOMUtil.getNamespaceURI(element);
    String elName = DOMUtil.getLocalName(element);
    if (!SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(uri)) {
        reportSchemaError("s4s-elt-schema-ns", new Object[] { elName }, element);
    }
    Hashtable eleAttrsMap = fEleAttrsMapG;
    String lookupName = elName;
    // REVISIT: only local element and attribute are different from others.  
    //          it's possible to have either name or ref. all the others  
    //          are only allowed to have one of name or ref, or neither of them.  
    //          we'd better move such checking to the traverser.  
    if (!isGlobal) {
        eleAttrsMap = fEleAttrsMapL;
        if (elName.equals(SchemaSymbols.ELT_ELEMENT)) {
            if (DOMUtil.getAttr(element, SchemaSymbols.ATT_REF) != null)
                lookupName = ELEMENT_R;
            else
                lookupName = ELEMENT_N;
        } else if (elName.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
            if (DOMUtil.getAttr(element, SchemaSymbols.ATT_REF) != null)
                lookupName = ATTRIBUTE_R;
            else
                lookupName = ATTRIBUTE_N;
        }
    }
    // get desired attribute list of this element  
    Container attrList = (Container) eleAttrsMap.get(lookupName);
    if (attrList == null) {
        // should never gets here.  
        // when this method is called, the call already knows that  
        // the element can appear.  
        reportSchemaError("s4s-elt-invalid", new Object[] { elName }, element);
        return null;
    }
    //Hashtable attrValues = new Hashtable();  
    Object[] attrValues = getAvailableArray();
    //Hashtable otherValues = new Hashtable();  
    long fromDefault = 0;
    // clear the "seen" flag.  
    System.arraycopy(fSeenTemp, 0, fSeen, 0, ATTIDX_COUNT);
    // traverse all attributes  
    int length = attrs.length;
    Attr sattr = null;
    for (int i = 0; i < length; i++) {
        sattr = attrs[i];
        // get the attribute name/value  
        //String attrName = DOMUtil.getLocalName(sattr);  
        String attrName = sattr.getName();
        String attrURI = DOMUtil.getNamespaceURI(sattr);
        String attrVal = DOMUtil.getValue(sattr);
        if (attrName.startsWith("xml")) {
            String attrPrefix = DOMUtil.getPrefix(sattr);
            // we don't want to add namespace declarations to the non-schema attributes  
            if ("xmlns".equals(attrPrefix) || "xmlns".equals(attrName)) {
                continue;
            } else if (SchemaSymbols.ATT_XML_LANG.equals(attrName) && (SchemaSymbols.ELT_SCHEMA.equals(elName) || SchemaSymbols.ELT_DOCUMENTATION.equals(elName))) {
                attrURI = null;
            }
        }
        // for attributes with namespace prefix  
        //  
        if (attrURI != null && attrURI.length() != 0) {
            // attributes with schema namespace are not allowed  
            // and not allowed on "document" and "appInfo"  
            if (attrURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
                reportSchemaError("s4s-att-not-allowed", new Object[] { elName, attrName }, element);
            } else {
                if (attrValues[ATTIDX_NONSCHEMA] == null) {
                    // these are usually small  
                    attrValues[ATTIDX_NONSCHEMA] = new Vector(4, 2);
                }
                ((Vector) attrValues[ATTIDX_NONSCHEMA]).addElement(attrName);
                ((Vector) attrValues[ATTIDX_NONSCHEMA]).addElement(attrVal);
            }
            continue;
        }
        // check whether this attribute is allowed  
        OneAttr oneAttr = attrList.get(attrName);
        if (oneAttr == null) {
            reportSchemaError("s4s-att-not-allowed", new Object[] { elName, attrName }, element);
            continue;
        }
        // we've seen this attribute  
        fSeen[oneAttr.valueIndex] = true;
        // check the value against the datatype  
        try {
            // no checking on string needs to be done here.  
            // no checking on xpath needs to be done here.  
            // xpath values are validated in xpath parser  
            if (oneAttr.dvIndex >= 0) {
                if (oneAttr.dvIndex != DT_STRING && oneAttr.dvIndex != DT_XPATH && oneAttr.dvIndex != DT_XPATH1) {
                    XSSimpleType dv = fExtraDVs[oneAttr.dvIndex];
                    Object avalue = dv.validate(attrVal, schemaDoc.fValidationContext, null);
                    // kludge to handle chameleon includes/redefines...  
                    if (oneAttr.dvIndex == DT_QNAME) {
                        QName qname = (QName) avalue;
                        if (qname.prefix == XMLSymbols.EMPTY_STRING && qname.uri == null && schemaDoc.fIsChameleonSchema)
                            qname.uri = schemaDoc.fTargetNamespace;
                    }
                    attrValues[oneAttr.valueIndex] = avalue;
                } else {
                    attrValues[oneAttr.valueIndex] = attrVal;
                }
            } else {
                attrValues[oneAttr.valueIndex] = validate(attrValues, attrName, attrVal, oneAttr.dvIndex, schemaDoc);
            }
        } catch (InvalidDatatypeValueException ide) {
            reportSchemaError("s4s-att-invalid-value", new Object[] { elName, attrName, ide.getMessage() }, element);
            if (oneAttr.dfltValue != null)
                //attrValues.put(attrName, oneAttr.dfltValue);  
                attrValues[oneAttr.valueIndex] = oneAttr.dfltValue;
        }
        // For "enumeration", and type is possible to be a QName, we need  
        // to return namespace context for later QName resolution.  
        if (elName.equals(SchemaSymbols.ELT_ENUMERATION) && enumAsQName) {
            attrValues[ATTIDX_ENUMNSDECLS] = new SchemaNamespaceSupport(schemaDoc.fNamespaceSupport);
        }
    }
    // apply default values  
    OneAttr[] reqAttrs = attrList.values;
    for (int i = 0; i < reqAttrs.length; i++) {
        OneAttr oneAttr = reqAttrs[i];
        // if the attribute didn't apprear, and  
        // if the attribute is optional with default value, apply it  
        if (oneAttr.dfltValue != null && !fSeen[oneAttr.valueIndex]) {
            //attrValues.put(oneAttr.name, oneAttr.dfltValue);  
            attrValues[oneAttr.valueIndex] = oneAttr.dfltValue;
            fromDefault |= (1 << oneAttr.valueIndex);
        }
    }
    attrValues[ATTIDX_FROMDEFAULT] = new Long(fromDefault);
    //attrValues[ATTIDX_OTHERVALUES] = otherValues;  
    // Check that minOccurs isn't greater than maxOccurs.  
    // p-props-correct 2.1  
    if (attrValues[ATTIDX_MAXOCCURS] != null) {
        int min = ((XInt) attrValues[ATTIDX_MINOCCURS]).intValue();
        int max = ((XInt) attrValues[ATTIDX_MAXOCCURS]).intValue();
        if (max != SchemaSymbols.OCCURRENCE_UNBOUNDED) {
            if (min > max) {
                reportSchemaError("p-props-correct.2.1", new Object[] { elName, attrValues[ATTIDX_MINOCCURS], attrValues[ATTIDX_MAXOCCURS] }, element);
                attrValues[ATTIDX_MINOCCURS] = attrValues[ATTIDX_MAXOCCURS];
            }
        }
    }
    return attrValues;
}

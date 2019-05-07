//REVISIT:we can convert into functions.. adding default attribute values.. and one validating.  
/** Add default attributes and validate. */
protected void addDTDDefaultAttrsAndValidate(QName elementName, int elementIndex, XMLAttributes attributes) throws XNIException {
    // is there anything to do?  
    if (elementIndex == -1 || fDTDGrammar == null) {
        return;
    }
    //  
    // Check after all specified attrs are scanned  
    // (1) report error for REQUIRED attrs that are missing (V_TAGc)  
    // (2) add default attrs (FIXED and NOT_FIXED)  
    //  
    int attlistIndex = fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
    while (attlistIndex != -1) {
        fDTDGrammar.getAttributeDecl(attlistIndex, fTempAttDecl);
        if (DEBUG_ATTRIBUTES) {
            if (fTempAttDecl != null) {
                XMLElementDecl elementDecl = new XMLElementDecl();
                fDTDGrammar.getElementDecl(elementIndex, elementDecl);
                System.out.println("element: " + (elementDecl.name.localpart));
                System.out.println("attlistIndex " + attlistIndex + "\n" + "attName : '" + (fTempAttDecl.name.localpart) + "'\n" + "attType : " + fTempAttDecl.simpleType.type + "\n" + "attDefaultType : " + fTempAttDecl.simpleType.defaultType + "\n" + "attDefaultValue : '" + fTempAttDecl.simpleType.defaultValue + "'\n" + attributes.getLength() + "\n");
            }
        }
        String attPrefix = fTempAttDecl.name.prefix;
        String attLocalpart = fTempAttDecl.name.localpart;
        String attRawName = fTempAttDecl.name.rawname;
        String attType = getAttributeTypeName(fTempAttDecl);
        int attDefaultType = fTempAttDecl.simpleType.defaultType;
        String attValue = null;
        if (fTempAttDecl.simpleType.defaultValue != null) {
            attValue = fTempAttDecl.simpleType.defaultValue;
        }
        boolean specified = false;
        boolean required = attDefaultType == XMLSimpleType.DEFAULT_TYPE_REQUIRED;
        boolean cdata = attType == XMLSymbols.fCDATASymbol;
        if (!cdata || required || attValue != null) {
            int attrCount = attributes.getLength();
            for (int i = 0; i < attrCount; i++) {
                if (attributes.getQName(i) == attRawName) {
                    specified = true;
                    break;
                }
            }
        }
        if (!specified) {
            if (required) {
                if (fPerformValidation) {
                    Object[] args = { elementName.localpart, attRawName };
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_REQUIRED_ATTRIBUTE_NOT_SPECIFIED", args, XMLErrorReporter.SEVERITY_ERROR);
                }
            } else if (attValue != null) {
                if (fPerformValidation && fGrammarBucket.getStandalone()) {
                    if (fDTDGrammar.getAttributeDeclIsExternal(attlistIndex)) {
                        Object[] args = { elementName.localpart, attRawName };
                        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_DEFAULTED_ATTRIBUTE_NOT_SPECIFIED", args, XMLErrorReporter.SEVERITY_ERROR);
                    }
                }
                // add namespace information  
                if (fNamespaces) {
                    int index = attRawName.indexOf(':');
                    if (index != -1) {
                        attPrefix = attRawName.substring(0, index);
                        attPrefix = fSymbolTable.addSymbol(attPrefix);
                        attLocalpart = attRawName.substring(index + 1);
                        attLocalpart = fSymbolTable.addSymbol(attLocalpart);
                    }
                }
                // add attribute  
                fTempQName.setValues(attPrefix, attLocalpart, attRawName, fTempAttDecl.name.uri);
                int newAttr = attributes.addAttribute(fTempQName, attType, attValue);
            }
        }
        // get next att decl in the Grammar for this element  
        attlistIndex = fDTDGrammar.getNextAttributeDeclIndex(attlistIndex);
    }
    // now iterate through the expanded attributes for  
    // 1. if every attribute seen is declared in the DTD  
    // 2. check if the VC: default_fixed holds  
    // 3. validate every attribute.  
    int attrCount = attributes.getLength();
    for (int i = 0; i < attrCount; i++) {
        String attrRawName = attributes.getQName(i);
        boolean declared = false;
        if (fPerformValidation) {
            if (fGrammarBucket.getStandalone()) {
                // check VC: Standalone Document Declaration, entities  
                // references appear in the document.  
                // REVISIT: this can be combined to a single check in  
                // startEntity if we add one more argument in  
                // startEnity, inAttrValue  
                String nonNormalizedValue = attributes.getNonNormalizedValue(i);
                if (nonNormalizedValue != null) {
                    String entityName = getExternalEntityRefInAttrValue(nonNormalizedValue);
                    if (entityName != null) {
                        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[] { entityName }, XMLErrorReporter.SEVERITY_ERROR);
                    }
                }
            }
        }
        int attDefIndex = -1;
        int position = fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
        while (position != -1) {
            fDTDGrammar.getAttributeDecl(position, fTempAttDecl);
            if (fTempAttDecl.name.rawname == attrRawName) {
                // found the match att decl,   
                attDefIndex = position;
                declared = true;
                break;
            }
            position = fDTDGrammar.getNextAttributeDeclIndex(position);
        }
        if (!declared) {
            if (fPerformValidation) {
                // REVISIT - cache the elem/attr tuple so that we only  
                // give this error once for each unique occurrence  
                Object[] args = { elementName.rawname, attrRawName };
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_ATTRIBUTE_NOT_DECLARED", args, XMLErrorReporter.SEVERITY_ERROR);
            }
            continue;
        }
        // attribute is declared  
        // fTempAttDecl should have the right value set now, so  
        // the following is not needed  
        // fGrammar.getAttributeDecl(attDefIndex,fTempAttDecl);  
        String type = getAttributeTypeName(fTempAttDecl);
        attributes.setType(i, type);
        attributes.getAugmentations(i).putItem(Constants.ATTRIBUTE_DECLARED, Boolean.TRUE);
        boolean changedByNormalization = false;
        String oldValue = attributes.getValue(i);
        String attrValue = oldValue;
        if (attributes.isSpecified(i) && type != XMLSymbols.fCDATASymbol) {
            changedByNormalization = normalizeAttrValue(attributes, i);
            attrValue = attributes.getValue(i);
            if (fPerformValidation && fGrammarBucket.getStandalone() && changedByNormalization && fDTDGrammar.getAttributeDeclIsExternal(position)) {
                // check VC: Standalone Document Declaration  
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE", new Object[] { attrRawName, oldValue, attrValue }, XMLErrorReporter.SEVERITY_ERROR);
            }
        }
        if (!fPerformValidation) {
            continue;
        }
        if (fTempAttDecl.simpleType.defaultType == XMLSimpleType.DEFAULT_TYPE_FIXED) {
            String defaultValue = fTempAttDecl.simpleType.defaultValue;
            if (!attrValue.equals(defaultValue)) {
                Object[] args = { elementName.localpart, attrRawName, attrValue, defaultValue };
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_FIXED_ATTVALUE_INVALID", args, XMLErrorReporter.SEVERITY_ERROR);
            }
        }
        if (fTempAttDecl.simpleType.type == XMLSimpleType.TYPE_ENTITY || fTempAttDecl.simpleType.type == XMLSimpleType.TYPE_ENUMERATION || fTempAttDecl.simpleType.type == XMLSimpleType.TYPE_ID || fTempAttDecl.simpleType.type == XMLSimpleType.TYPE_IDREF || fTempAttDecl.simpleType.type == XMLSimpleType.TYPE_NMTOKEN || fTempAttDecl.simpleType.type == XMLSimpleType.TYPE_NOTATION) {
            validateDTDattribute(elementName, attrValue, fTempAttDecl);
        }
    }
}

void addDefaultAttributes(QName element, XMLAttributes attributes, XSAttributeGroupDecl attrGrp) {
    // Check after all specified attrs are scanned  
    // (1) report error for REQUIRED attrs that are missing (V_TAGc)  
    // REVISIT: should we check prohibited attributes?  
    // (2) report error for PROHIBITED attrs that are present (V_TAGc)  
    // (3) add default attrs (FIXED and NOT_FIXED)  
    //  
    if (DEBUG) {
        System.out.println("==>addDefaultAttributes: " + element);
    }
    XSObjectList attrUses = attrGrp.getAttributeUses();
    int useCount = attrUses.getLength();
    XSAttributeUseImpl currUse;
    XSAttributeDecl currDecl;
    short constType;
    ValidatedInfo defaultValue;
    boolean isSpecified;
    QName attName;
    // for each attribute use  
    for (int i = 0; i < useCount; i++) {
        currUse = (XSAttributeUseImpl) attrUses.item(i);
        currDecl = currUse.fAttrDecl;
        // get value constraint  
        constType = currUse.fConstraintType;
        defaultValue = currUse.fDefault;
        if (constType == XSConstants.VC_NONE) {
            constType = currDecl.getConstraintType();
            defaultValue = currDecl.fDefault;
        }
        // whether this attribute is specified  
        isSpecified = attributes.getValue(currDecl.fTargetNamespace, currDecl.fName) != null;
        // Element Locally Valid (Complex Type)  
        // 4 The {attribute declaration} of each attribute use in the {attribute uses} whose  
        // {required} is true matches one of the attribute information items in the element  
        // information item's [attributes] as per clause 3.1 above.  
        if (currUse.fUse == SchemaSymbols.USE_REQUIRED) {
            if (!isSpecified)
                reportSchemaError("cvc-complex-type.4", new Object[] { element.rawname, currDecl.fName });
        }
        // if the attribute is not specified, then apply the value constraint  
        if (!isSpecified && constType != XSConstants.VC_NONE) {
            attName = new QName(null, currDecl.fName, currDecl.fName, currDecl.fTargetNamespace);
            String normalized = (defaultValue != null) ? defaultValue.stringValue() : "";
            int attrIndex;
            if (attributes instanceof XMLAttributesImpl) {
                XMLAttributesImpl attrs = (XMLAttributesImpl) attributes;
                attrIndex = attrs.getLength();
                attrs.addAttributeNS(attName, "CDATA", normalized);
            } else {
                attrIndex = attributes.addAttribute(attName, "CDATA", normalized);
            }
            if (fAugPSVI) {
                // PSVI: attribute is "schema" specified  
                Augmentations augs = attributes.getAugmentations(attrIndex);
                AttributePSVImpl attrPSVI = new AttributePSVImpl();
                augs.putItem(Constants.ATTRIBUTE_PSVI, attrPSVI);
                attrPSVI.fDeclaration = currDecl;
                attrPSVI.fTypeDecl = currDecl.fType;
                attrPSVI.fMemberType = defaultValue.memberType;
                attrPSVI.fNormalizedValue = normalized;
                attrPSVI.fActualValue = defaultValue.actualValue;
                attrPSVI.fActualValueType = defaultValue.actualValueType;
                attrPSVI.fItemValueTypes = defaultValue.itemValueTypes;
                attrPSVI.fValidationContext = fValidationRoot;
                attrPSVI.fValidity = AttributePSVI.VALIDITY_VALID;
                attrPSVI.fValidationAttempted = AttributePSVI.VALIDATION_FULL;
                attrPSVI.fSpecified = true;
            }
        }
    }
}

void processAttributes(QName element, XMLAttributes attributes, XSAttributeGroupDecl attrGrp) {
    if (DEBUG) {
        System.out.println("==>processAttributes: " + attributes.getLength());
    }
    // whether we have seen a Wildcard ID.  
    String wildcardIDName = null;
    // for each present attribute  
    int attCount = attributes.getLength();
    Augmentations augs = null;
    AttributePSVImpl attrPSVI = null;
    boolean isSimple = fCurrentType == null || fCurrentType.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE;
    XSObjectList attrUses = null;
    int useCount = 0;
    XSWildcardDecl attrWildcard = null;
    if (!isSimple) {
        attrUses = attrGrp.getAttributeUses();
        useCount = attrUses.getLength();
        attrWildcard = attrGrp.fAttributeWC;
    }
    // Element Locally Valid (Complex Type)  
    // 3 For each attribute information item in the element information item's [attributes] excepting those whose [namespace name] is identical to http://www.w3.org/2001/XMLSchema-instance and whose [local name] is one of type, nil, schemaLocation or noNamespaceSchemaLocation, the appropriate case among the following must be true:  
    // get the corresponding attribute decl  
    for (int index = 0; index < attCount; index++) {
        attributes.getName(index, fTempQName);
        if (DEBUG) {
            System.out.println("==>process attribute: " + fTempQName);
        }
        if (fAugPSVI || fIdConstraint) {
            augs = attributes.getAugmentations(index);
            attrPSVI = (AttributePSVImpl) augs.getItem(Constants.ATTRIBUTE_PSVI);
            if (attrPSVI != null) {
                attrPSVI.reset();
            } else {
                attrPSVI = new AttributePSVImpl();
                augs.putItem(Constants.ATTRIBUTE_PSVI, attrPSVI);
            }
            // PSVI attribute: validation context  
            attrPSVI.fValidationContext = fValidationRoot;
        }
        // Element Locally Valid (Type)  
        // 3.1.1 The element information item's [attributes] must be empty, excepting those  
        // whose [namespace name] is identical to http://www.w3.org/2001/XMLSchema-instance and  
        // whose [local name] is one of type, nil, schemaLocation or noNamespaceSchemaLocation.  
        // for the 4 xsi attributes, get appropriate decl, and validate  
        if (fTempQName.uri == SchemaSymbols.URI_XSI) {
            XSAttributeDecl attrDecl = null;
            if (fTempQName.localpart == SchemaSymbols.XSI_TYPE) {
                attrDecl = XSI_TYPE;
            } else if (fTempQName.localpart == SchemaSymbols.XSI_NIL) {
                attrDecl = XSI_NIL;
            } else if (fTempQName.localpart == SchemaSymbols.XSI_SCHEMALOCATION) {
                attrDecl = XSI_SCHEMALOCATION;
            } else if (fTempQName.localpart == SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION) {
                attrDecl = XSI_NONAMESPACESCHEMALOCATION;
            }
            if (attrDecl != null) {
                processOneAttribute(element, attributes, index, attrDecl, null, attrPSVI);
                continue;
            }
        }
        // for namespace attributes, no_validation/unknow_validity  
        if (fTempQName.rawname == XMLSymbols.PREFIX_XMLNS || fTempQName.rawname.startsWith("xmlns:")) {
            continue;
        }
        // simple type doesn't allow any other attributes  
        if (isSimple) {
            reportSchemaError("cvc-type.3.1.1", new Object[] { element.rawname, fTempQName.rawname });
            continue;
        }
        // it's not xmlns, and not xsi, then we need to find a decl for it  
        XSAttributeUseImpl currUse = null, oneUse;
        for (int i = 0; i < useCount; i++) {
            oneUse = (XSAttributeUseImpl) attrUses.item(i);
            if (oneUse.fAttrDecl.fName == fTempQName.localpart && oneUse.fAttrDecl.fTargetNamespace == fTempQName.uri) {
                currUse = oneUse;
                break;
            }
        }
        // 3.2 otherwise all of the following must be true:  
        // 3.2.1 There must be an {attribute wildcard}.  
        // 3.2.2 The attribute information item must be valid with respect to it as defined in Item Valid (Wildcard) (3.10.4).  
        // if failed, get it from wildcard  
        if (currUse == null) {
            //if (attrWildcard == null)  
            //    reportSchemaError("cvc-complex-type.3.2.1", new Object[]{element.rawname, fTempQName.rawname});  
            if (attrWildcard == null || !attrWildcard.allowNamespace(fTempQName.uri)) {
                // so this attribute is not allowed  
                reportSchemaError("cvc-complex-type.3.2.2", new Object[] { element.rawname, fTempQName.rawname });
                // We have seen an attribute that was not declared  
                fNFullValidationDepth = fElementDepth;
                continue;
            }
        }
        XSAttributeDecl currDecl = null;
        if (currUse != null) {
            currDecl = currUse.fAttrDecl;
        } else {
            // which means it matches a wildcard  
            // skip it if processContents is skip  
            if (attrWildcard.fProcessContents == XSWildcardDecl.PC_SKIP)
                continue;
            //try to find grammar by different means...  
            SchemaGrammar grammar = findSchemaGrammar(XSDDescription.CONTEXT_ATTRIBUTE, fTempQName.uri, element, fTempQName, attributes);
            if (grammar != null) {
                currDecl = grammar.getGlobalAttributeDecl(fTempQName.localpart);
            }
            // if can't find  
            if (currDecl == null) {
                // if strict, report error  
                if (attrWildcard.fProcessContents == XSWildcardDecl.PC_STRICT) {
                    reportSchemaError("cvc-complex-type.3.2.2", new Object[] { element.rawname, fTempQName.rawname });
                }
                // then continue to the next attribute  
                continue;
            } else {
                // 5 Let [Definition:]  the wild IDs be the set of all attribute information item to which clause 3.2 applied and whose validation resulted in a context-determined declaration of mustFind or no context-determined declaration at all, and whose [local name] and [namespace name] resolve (as defined by QName resolution (Instance) (3.15.4)) to an attribute declaration whose {type definition} is or is derived from ID. Then all of the following must be true:  
                // 5.1 There must be no more than one item in wild IDs.  
                if (currDecl.fType.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE && ((XSSimpleType) currDecl.fType).isIDType()) {
                    if (wildcardIDName != null) {
                        reportSchemaError("cvc-complex-type.5.1", new Object[] { element.rawname, currDecl.fName, wildcardIDName });
                    } else
                        wildcardIDName = currDecl.fName;
                }
            }
        }
        processOneAttribute(element, attributes, index, currDecl, currUse, attrPSVI);
    }
    // end of for (all attributes)  
    // 5.2 If wild IDs is non-empty, there must not be any attribute uses among the {attribute uses} whose {attribute declaration}'s {type definition} is or is derived from ID.  
    if (!isSimple && attrGrp.fIDAttrName != null && wildcardIDName != null) {
        reportSchemaError("cvc-complex-type.5.2", new Object[] { element.rawname, wildcardIDName, attrGrp.fIDAttrName });
    }
}

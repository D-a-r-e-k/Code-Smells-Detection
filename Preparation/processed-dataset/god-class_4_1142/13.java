// This method builds registries for all globally-referenceable  
// names.  A registry will be built for each symbol space defined  
// by the spec.  It is also this method's job to rename redefined  
// components, and to record which components redefine others (so  
// that implicit redefinitions of groups and attributeGroups can be handled).  
protected void buildGlobalNameRegistries() {
    // Starting with fRoot, we examine each child of the schema  
    // element.  Skipping all imports and includes, we record the names  
    // of all other global components (and children of <redefine>).  We  
    // also put <redefine> names in a registry that we look through in  
    // case something needs renaming.  Once we're done with a schema we  
    // set its Document node to hidden so that we don't try to traverse  
    // it again; then we look to its Dependency map entry.  We keep a  
    // stack of schemas that we haven't yet finished processing; this  
    // is a depth-first traversal.  
    Stack schemasToProcess = new Stack();
    schemasToProcess.push(fRoot);
    while (!schemasToProcess.empty()) {
        XSDocumentInfo currSchemaDoc = (XSDocumentInfo) schemasToProcess.pop();
        Element currDoc = currSchemaDoc.fSchemaElement;
        if (DOMUtil.isHidden(currDoc, fHiddenNodes)) {
            // must have processed this already!  
            continue;
        }
        Element currRoot = currDoc;
        // process this schema's global decls  
        boolean dependenciesCanOccur = true;
        for (Element globalComp = DOMUtil.getFirstChildElement(currRoot); globalComp != null; globalComp = DOMUtil.getNextSiblingElement(globalComp)) {
            // this loop makes sure the <schema> element ordering is  
            // also valid.  
            if (DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_ANNOTATION)) {
                //skip it; traverse it later  
                continue;
            } else if (DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_INCLUDE) || DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_IMPORT)) {
                if (!dependenciesCanOccur) {
                    reportSchemaError("s4s-elt-invalid-content.3", new Object[] { DOMUtil.getLocalName(globalComp) }, globalComp);
                }
                DOMUtil.setHidden(globalComp, fHiddenNodes);
            } else if (DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_REDEFINE)) {
                if (!dependenciesCanOccur) {
                    reportSchemaError("s4s-elt-invalid-content.3", new Object[] { DOMUtil.getLocalName(globalComp) }, globalComp);
                }
                for (Element redefineComp = DOMUtil.getFirstChildElement(globalComp); redefineComp != null; redefineComp = DOMUtil.getNextSiblingElement(redefineComp)) {
                    String lName = DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME);
                    if (lName.length() == 0)
                        // an error we'll catch later  
                        continue;
                    String qName = currSchemaDoc.fTargetNamespace == null ? "," + lName : currSchemaDoc.fTargetNamespace + "," + lName;
                    String componentType = DOMUtil.getLocalName(redefineComp);
                    if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                        checkForDuplicateNames(qName, ATTRIBUTEGROUP_TYPE, fUnparsedAttributeGroupRegistry, fUnparsedAttributeGroupRegistrySub, redefineComp, currSchemaDoc);
                        // the check will have changed our name;  
                        String targetLName = DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME) + REDEF_IDENTIFIER;
                        // and all we need to do is error-check+rename our kkids:  
                        renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_ATTRIBUTEGROUP, lName, targetLName);
                    } else if ((componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) || (componentType.equals(SchemaSymbols.ELT_SIMPLETYPE))) {
                        checkForDuplicateNames(qName, TYPEDECL_TYPE, fUnparsedTypeRegistry, fUnparsedTypeRegistrySub, redefineComp, currSchemaDoc);
                        // the check will have changed our name;  
                        String targetLName = DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME) + REDEF_IDENTIFIER;
                        // and all we need to do is error-check+rename our kkids:  
                        if (componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                            renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_COMPLEXTYPE, lName, targetLName);
                        } else {
                            // must be simpleType  
                            renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_SIMPLETYPE, lName, targetLName);
                        }
                    } else if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
                        checkForDuplicateNames(qName, GROUP_TYPE, fUnparsedGroupRegistry, fUnparsedGroupRegistrySub, redefineComp, currSchemaDoc);
                        // the check will have changed our name;  
                        String targetLName = DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME) + REDEF_IDENTIFIER;
                        // and all we need to do is error-check+rename our kids:  
                        renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_GROUP, lName, targetLName);
                    }
                }
            } else {
                dependenciesCanOccur = false;
                String lName = DOMUtil.getAttrValue(globalComp, SchemaSymbols.ATT_NAME);
                if (lName.length() == 0)
                    // an error we'll catch later  
                    continue;
                String qName = currSchemaDoc.fTargetNamespace == null ? "," + lName : currSchemaDoc.fTargetNamespace + "," + lName;
                String componentType = DOMUtil.getLocalName(globalComp);
                if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                    checkForDuplicateNames(qName, ATTRIBUTE_TYPE, fUnparsedAttributeRegistry, fUnparsedAttributeRegistrySub, globalComp, currSchemaDoc);
                } else if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                    checkForDuplicateNames(qName, ATTRIBUTEGROUP_TYPE, fUnparsedAttributeGroupRegistry, fUnparsedAttributeGroupRegistrySub, globalComp, currSchemaDoc);
                } else if ((componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) || (componentType.equals(SchemaSymbols.ELT_SIMPLETYPE))) {
                    checkForDuplicateNames(qName, TYPEDECL_TYPE, fUnparsedTypeRegistry, fUnparsedTypeRegistrySub, globalComp, currSchemaDoc);
                } else if (componentType.equals(SchemaSymbols.ELT_ELEMENT)) {
                    checkForDuplicateNames(qName, ELEMENT_TYPE, fUnparsedElementRegistry, fUnparsedElementRegistrySub, globalComp, currSchemaDoc);
                } else if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
                    checkForDuplicateNames(qName, GROUP_TYPE, fUnparsedGroupRegistry, fUnparsedGroupRegistrySub, globalComp, currSchemaDoc);
                } else if (componentType.equals(SchemaSymbols.ELT_NOTATION)) {
                    checkForDuplicateNames(qName, NOTATION_TYPE, fUnparsedNotationRegistry, fUnparsedNotationRegistrySub, globalComp, currSchemaDoc);
                }
            }
        }
        // end for  
        // now we're done with this one!  
        DOMUtil.setHidden(currDoc, fHiddenNodes);
        // now add the schemas this guy depends on  
        Vector currSchemaDepends = (Vector) fDependencyMap.get(currSchemaDoc);
        for (int i = 0; i < currSchemaDepends.size(); i++) {
            schemasToProcess.push(currSchemaDepends.elementAt(i));
        }
    }
}

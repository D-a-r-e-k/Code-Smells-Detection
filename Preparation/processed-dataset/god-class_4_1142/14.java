// end buildGlobalNameRegistries  
// Beginning at the first schema processing was requested for  
// (fRoot), this method  
// examines each child (global schema information item) of each  
// schema document (and of each <redefine> element)  
// corresponding to an XSDocumentInfo object.  If the  
// readOnly field on that node has not been set, it calls an  
// appropriate traverser to traverse it.  Once all global decls in  
// an XSDocumentInfo object have been traversed, it marks that object  
// as traversed (or hidden) in order to avoid infinite loops.  It completes  
// when it has visited all XSDocumentInfo objects in the  
// DependencyMap and marked them as traversed.  
protected void traverseSchemas(ArrayList annotationInfo) {
    // the process here is very similar to that in  
    // buildGlobalRegistries, except we can't set our schemas as  
    // hidden for a second time; so make them all visible again  
    // first!  
    setSchemasVisible(fRoot);
    Stack schemasToProcess = new Stack();
    schemasToProcess.push(fRoot);
    while (!schemasToProcess.empty()) {
        XSDocumentInfo currSchemaDoc = (XSDocumentInfo) schemasToProcess.pop();
        Element currDoc = currSchemaDoc.fSchemaElement;
        SchemaGrammar currSG = fGrammarBucket.getGrammar(currSchemaDoc.fTargetNamespace);
        if (DOMUtil.isHidden(currDoc, fHiddenNodes)) {
            // must have processed this already!  
            continue;
        }
        Element currRoot = currDoc;
        boolean sawAnnotation = false;
        // traverse this schema's global decls  
        for (Element globalComp = DOMUtil.getFirstVisibleChildElement(currRoot, fHiddenNodes); globalComp != null; globalComp = DOMUtil.getNextVisibleSiblingElement(globalComp, fHiddenNodes)) {
            DOMUtil.setHidden(globalComp, fHiddenNodes);
            String componentType = DOMUtil.getLocalName(globalComp);
            // includes and imports will not show up here!  
            if (DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_REDEFINE)) {
                // use the namespace decls for the redefine, instead of for the parent <schema>  
                currSchemaDoc.backupNSSupport((SchemaNamespaceSupport) fRedefine2NSSupport.get(globalComp));
                for (Element redefinedComp = DOMUtil.getFirstVisibleChildElement(globalComp, fHiddenNodes); redefinedComp != null; redefinedComp = DOMUtil.getNextVisibleSiblingElement(redefinedComp, fHiddenNodes)) {
                    String redefinedComponentType = DOMUtil.getLocalName(redefinedComp);
                    DOMUtil.setHidden(redefinedComp, fHiddenNodes);
                    if (redefinedComponentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                        fAttributeGroupTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
                    } else if (redefinedComponentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                        fComplexTypeTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
                    } else if (redefinedComponentType.equals(SchemaSymbols.ELT_GROUP)) {
                        fGroupTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
                    } else if (redefinedComponentType.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                        fSimpleTypeTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
                    } else {
                        reportSchemaError("s4s-elt-must-match.1", new Object[] { DOMUtil.getLocalName(globalComp), "(annotation | (simpleType | complexType | group | attributeGroup))*", redefinedComponentType }, redefinedComp);
                    }
                }
                // end march through <redefine> children  
                currSchemaDoc.restoreNSSupport();
            } else if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                fAttributeTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
            } else if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                fAttributeGroupTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
            } else if (componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                fComplexTypeTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
            } else if (componentType.equals(SchemaSymbols.ELT_ELEMENT)) {
                fElementTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
            } else if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
                fGroupTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
            } else if (componentType.equals(SchemaSymbols.ELT_NOTATION)) {
                fNotationTraverser.traverse(globalComp, currSchemaDoc, currSG);
            } else if (componentType.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                fSimpleTypeTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
            } else if (componentType.equals(SchemaSymbols.ELT_ANNOTATION)) {
                currSG.addAnnotation(fElementTraverser.traverseAnnotationDecl(globalComp, currSchemaDoc.getSchemaAttrs(), true, currSchemaDoc));
                sawAnnotation = true;
            } else {
                reportSchemaError("s4s-elt-invalid-content.1", new Object[] { SchemaSymbols.ELT_SCHEMA, DOMUtil.getLocalName(globalComp) }, globalComp);
            }
        }
        // end for  
        if (!sawAnnotation) {
            String text = DOMUtil.getSyntheticAnnotation(currRoot);
            if (text != null) {
                currSG.addAnnotation(fElementTraverser.traverseSyntheticAnnotation(currRoot, text, currSchemaDoc.getSchemaAttrs(), true, currSchemaDoc));
            }
        }
        /** Collect annotation information for validation. **/
        if (annotationInfo != null) {
            XSAnnotationInfo info = currSchemaDoc.getAnnotations();
            /** Only add annotations to the list if there were any in this document. **/
            if (info != null) {
                annotationInfo.add(doc2SystemId(currDoc));
                annotationInfo.add(info);
            }
        }
        // now we're done with this one!  
        currSchemaDoc.returnSchemaAttrs();
        DOMUtil.setHidden(currDoc, fHiddenNodes);
        // now add the schemas this guy depends on  
        Vector currSchemaDepends = (Vector) fDependencyMap.get(currSchemaDoc);
        for (int i = 0; i < currSchemaDepends.size(); i++) {
            schemasToProcess.push(currSchemaDepends.elementAt(i));
        }
    }
}

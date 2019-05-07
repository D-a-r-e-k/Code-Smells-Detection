// This method does several things:  
// It constructs an instance of an XSDocumentInfo object using the  
// schemaRoot node.  Then, for each <include>,  
// <redefine>, and <import> children, it attempts to resolve the  
// requested schema document, initiates a DOM parse, and calls  
// itself recursively on that document's root.  It also records in  
// the DependencyMap object what XSDocumentInfo objects its XSDocumentInfo  
// depends on.  
// It also makes sure the targetNamespace of the schema it was  
// called to parse is correct.  
protected XSDocumentInfo constructTrees(Element schemaRoot, String locationHint, XSDDescription desc, boolean nsCollision) {
    if (schemaRoot == null)
        return null;
    String callerTNS = desc.getTargetNamespace();
    short referType = desc.getContextType();
    XSDocumentInfo currSchemaInfo = null;
    try {
        // note that attributes are freed at end of traverseSchemas()  
        currSchemaInfo = new XSDocumentInfo(schemaRoot, fAttributeChecker, fSymbolTable);
    } catch (XMLSchemaException se) {
        reportSchemaError(ELE_ERROR_CODES[referType], new Object[] { locationHint }, schemaRoot);
        return null;
    }
    // targetNamespace="" is not valid, issue a warning, and ignore it  
    if (currSchemaInfo.fTargetNamespace != null && currSchemaInfo.fTargetNamespace.length() == 0) {
        reportSchemaWarning("EmptyTargetNamespace", new Object[] { locationHint }, schemaRoot);
        currSchemaInfo.fTargetNamespace = null;
    }
    if (callerTNS != null) {
        // the second index to the NS_ERROR_CODES array  
        // if the caller/expected NS is not absent, we use the first column  
        int secondIdx = 0;
        // for include and redefine  
        if (referType == XSDDescription.CONTEXT_INCLUDE || referType == XSDDescription.CONTEXT_REDEFINE) {
            // if the referred document has no targetNamespace,  
            // it's a chameleon schema  
            if (currSchemaInfo.fTargetNamespace == null) {
                currSchemaInfo.fTargetNamespace = callerTNS;
                currSchemaInfo.fIsChameleonSchema = true;
            } else if (callerTNS != currSchemaInfo.fTargetNamespace) {
                reportSchemaError(NS_ERROR_CODES[referType][secondIdx], new Object[] { callerTNS, currSchemaInfo.fTargetNamespace }, schemaRoot);
                return null;
            }
        } else if (referType != XSDDescription.CONTEXT_PREPARSE && callerTNS != currSchemaInfo.fTargetNamespace) {
            reportSchemaError(NS_ERROR_CODES[referType][secondIdx], new Object[] { callerTNS, currSchemaInfo.fTargetNamespace }, schemaRoot);
            return null;
        }
    } else if (currSchemaInfo.fTargetNamespace != null) {
        // set the target namespace of the description  
        if (referType == XSDDescription.CONTEXT_PREPARSE) {
            desc.setTargetNamespace(currSchemaInfo.fTargetNamespace);
            callerTNS = currSchemaInfo.fTargetNamespace;
        } else {
            // the second index to the NS_ERROR_CODES array  
            // if the caller/expected NS is absent, we use the second column  
            int secondIdx = 1;
            reportSchemaError(NS_ERROR_CODES[referType][secondIdx], new Object[] { callerTNS, currSchemaInfo.fTargetNamespace }, schemaRoot);
            return null;
        }
    }
    // the other cases (callerTNS == currSchemaInfo.fTargetNamespce == null)  
    // are valid  
    // a schema document can always access it's own target namespace  
    currSchemaInfo.addAllowedNS(currSchemaInfo.fTargetNamespace);
    SchemaGrammar sg = null;
    // we have a namespace collision  
    if (nsCollision) {
        SchemaGrammar sg2 = fGrammarBucket.getGrammar(currSchemaInfo.fTargetNamespace);
        if (sg2.isImmutable()) {
            sg = new SchemaGrammar(sg2);
            fGrammarBucket.putGrammar(sg);
            // update all the grammars in the bucket to point to the new grammar.  
            updateImportListWith(sg);
        } else {
            sg = sg2;
        }
        // update import list of the new grammar  
        updateImportListFor(sg);
    } else if (referType == XSDDescription.CONTEXT_INCLUDE || referType == XSDDescription.CONTEXT_REDEFINE) {
        sg = fGrammarBucket.getGrammar(currSchemaInfo.fTargetNamespace);
    } else if (fHonourAllSchemaLocations && referType == XSDDescription.CONTEXT_IMPORT) {
        sg = findGrammar(desc, false);
        if (sg == null) {
            sg = new SchemaGrammar(currSchemaInfo.fTargetNamespace, desc.makeClone(), fSymbolTable);
            fGrammarBucket.putGrammar(sg);
        }
    } else {
        sg = new SchemaGrammar(currSchemaInfo.fTargetNamespace, desc.makeClone(), fSymbolTable);
        fGrammarBucket.putGrammar(sg);
    }
    // store the document and its location  
    // REVISIT: don't expose the DOM tree  
    sg.addDocument(null, (String) fDoc2SystemId.get(currSchemaInfo.fSchemaElement));
    fDoc2XSDocumentMap.put(schemaRoot, currSchemaInfo);
    Vector dependencies = new Vector();
    Element rootNode = schemaRoot;
    Element newSchemaRoot = null;
    for (Element child = DOMUtil.getFirstChildElement(rootNode); child != null; child = DOMUtil.getNextSiblingElement(child)) {
        String schemaNamespace = null;
        String schemaHint = null;
        String localName = DOMUtil.getLocalName(child);
        short refType = -1;
        boolean importCollision = false;
        if (localName.equals(SchemaSymbols.ELT_ANNOTATION))
            continue;
        else if (localName.equals(SchemaSymbols.ELT_IMPORT)) {
            refType = XSDDescription.CONTEXT_IMPORT;
            // have to handle some validation here too!  
            // call XSAttributeChecker to fill in attrs  
            Object[] importAttrs = fAttributeChecker.checkAttributes(child, true, currSchemaInfo);
            schemaHint = (String) importAttrs[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
            schemaNamespace = (String) importAttrs[XSAttributeChecker.ATTIDX_NAMESPACE];
            if (schemaNamespace != null)
                schemaNamespace = fSymbolTable.addSymbol(schemaNamespace);
            // check contents and process optional annotations  
            Element importChild = DOMUtil.getFirstChildElement(child);
            if (importChild != null) {
                String importComponentType = DOMUtil.getLocalName(importChild);
                if (importComponentType.equals(SchemaSymbols.ELT_ANNOTATION)) {
                    // promoting annotations to parent component  
                    sg.addAnnotation(fElementTraverser.traverseAnnotationDecl(importChild, importAttrs, true, currSchemaInfo));
                } else {
                    reportSchemaError("s4s-elt-must-match.1", new Object[] { localName, "annotation?", importComponentType }, child);
                }
                if (DOMUtil.getNextSiblingElement(importChild) != null) {
                    reportSchemaError("s4s-elt-must-match.1", new Object[] { localName, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(importChild)) }, child);
                }
            } else {
                String text = DOMUtil.getSyntheticAnnotation(child);
                if (text != null) {
                    sg.addAnnotation(fElementTraverser.traverseSyntheticAnnotation(child, text, importAttrs, true, currSchemaInfo));
                }
            }
            fAttributeChecker.returnAttrArray(importAttrs, currSchemaInfo);
            // a document can't import another document with the same namespace  
            if (schemaNamespace == currSchemaInfo.fTargetNamespace) {
                reportSchemaError(schemaNamespace != null ? "src-import.1.1" : "src-import.1.2", new Object[] { schemaNamespace }, child);
                continue;
            }
            // if this namespace has not been imported by this document,  
            //  then import if multiple imports support is enabled.  
            if (currSchemaInfo.isAllowedNS(schemaNamespace)) {
                if (!fHonourAllSchemaLocations && !fNamespaceGrowth)
                    continue;
            } else {
                currSchemaInfo.addAllowedNS(schemaNamespace);
            }
            // also record the fact that one namespace imports another one  
            // convert null to ""  
            String tns = null2EmptyString(currSchemaInfo.fTargetNamespace);
            // get all namespaces imported by this one  
            Vector ins = (Vector) fImportMap.get(tns);
            // if no namespace was imported, create new Vector  
            if (ins == null) {
                // record that this one imports other(s)  
                fAllTNSs.addElement(tns);
                ins = new Vector();
                fImportMap.put(tns, ins);
                ins.addElement(schemaNamespace);
            } else if (!ins.contains(schemaNamespace)) {
                ins.addElement(schemaNamespace);
            }
            fSchemaGrammarDescription.reset();
            fSchemaGrammarDescription.setContextType(XSDDescription.CONTEXT_IMPORT);
            fSchemaGrammarDescription.setBaseSystemId(doc2SystemId(schemaRoot));
            fSchemaGrammarDescription.setLiteralSystemId(schemaHint);
            fSchemaGrammarDescription.setLocationHints(new String[] { schemaHint });
            fSchemaGrammarDescription.setTargetNamespace(schemaNamespace);
            // if a grammar with the same namespace and location exists (or being  
            // built), ignore this one (don't traverse it).  
            SchemaGrammar isg = findGrammar(fSchemaGrammarDescription, fNamespaceGrowth);
            if (isg != null) {
                if (fNamespaceGrowth) {
                    try {
                        if (isg.getDocumentLocations().contains(XMLEntityManager.expandSystemId(schemaHint, fSchemaGrammarDescription.getBaseSystemId(), false))) {
                            continue;
                        } else {
                            importCollision = true;
                        }
                    } catch (MalformedURIException e) {
                    }
                } else if (!fHonourAllSchemaLocations || isExistingGrammar(fSchemaGrammarDescription, false)) {
                    continue;
                }
            }
            //if ((!fHonourAllSchemaLocations && findGrammar(fSchemaGrammarDescription) != null) || isExistingGrammar(fSchemaGrammarDescription))  
            //    continue;  
            // If "findGrammar" returns a grammar, then this is not the  
            // the first time we see a location for a given namespace.  
            // Don't consult the location pair hashtable in this case,  
            // otherwise the location will be ignored because it'll get  
            // resolved to the same location as the first hint.  
            newSchemaRoot = resolveSchema(fSchemaGrammarDescription, false, child, isg == null);
        } else if ((localName.equals(SchemaSymbols.ELT_INCLUDE)) || (localName.equals(SchemaSymbols.ELT_REDEFINE))) {
            // validation for redefine/include will be the same here; just  
            // make sure TNS is right (don't care about redef contents  
            // yet).  
            Object[] includeAttrs = fAttributeChecker.checkAttributes(child, true, currSchemaInfo);
            schemaHint = (String) includeAttrs[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
            // store the namespace decls of the redefine element  
            if (localName.equals(SchemaSymbols.ELT_REDEFINE)) {
                fRedefine2NSSupport.put(child, new SchemaNamespaceSupport(currSchemaInfo.fNamespaceSupport));
            }
            // check annotations.  Must do this here to avoid having to  
            // re-parse attributes later  
            if (localName.equals(SchemaSymbols.ELT_INCLUDE)) {
                Element includeChild = DOMUtil.getFirstChildElement(child);
                if (includeChild != null) {
                    String includeComponentType = DOMUtil.getLocalName(includeChild);
                    if (includeComponentType.equals(SchemaSymbols.ELT_ANNOTATION)) {
                        // promoting annotations to parent component  
                        sg.addAnnotation(fElementTraverser.traverseAnnotationDecl(includeChild, includeAttrs, true, currSchemaInfo));
                    } else {
                        reportSchemaError("s4s-elt-must-match.1", new Object[] { localName, "annotation?", includeComponentType }, child);
                    }
                    if (DOMUtil.getNextSiblingElement(includeChild) != null) {
                        reportSchemaError("s4s-elt-must-match.1", new Object[] { localName, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(includeChild)) }, child);
                    }
                } else {
                    String text = DOMUtil.getSyntheticAnnotation(child);
                    if (text != null) {
                        sg.addAnnotation(fElementTraverser.traverseSyntheticAnnotation(child, text, includeAttrs, true, currSchemaInfo));
                    }
                }
            } else {
                for (Element redefinedChild = DOMUtil.getFirstChildElement(child); redefinedChild != null; redefinedChild = DOMUtil.getNextSiblingElement(redefinedChild)) {
                    String redefinedComponentType = DOMUtil.getLocalName(redefinedChild);
                    if (redefinedComponentType.equals(SchemaSymbols.ELT_ANNOTATION)) {
                        // promoting annotations to parent component  
                        sg.addAnnotation(fElementTraverser.traverseAnnotationDecl(redefinedChild, includeAttrs, true, currSchemaInfo));
                        DOMUtil.setHidden(redefinedChild, fHiddenNodes);
                    } else {
                        String text = DOMUtil.getSyntheticAnnotation(child);
                        if (text != null) {
                            sg.addAnnotation(fElementTraverser.traverseSyntheticAnnotation(child, text, includeAttrs, true, currSchemaInfo));
                        }
                    }
                }
            }
            fAttributeChecker.returnAttrArray(includeAttrs, currSchemaInfo);
            // schemaLocation is required on <include> and <redefine>  
            if (schemaHint == null) {
                reportSchemaError("s4s-att-must-appear", new Object[] { "<include> or <redefine>", "schemaLocation" }, child);
            }
            // pass the systemId of the current document as the base systemId  
            boolean mustResolve = false;
            refType = XSDDescription.CONTEXT_INCLUDE;
            if (localName.equals(SchemaSymbols.ELT_REDEFINE)) {
                mustResolve = nonAnnotationContent(child);
                refType = XSDDescription.CONTEXT_REDEFINE;
            }
            fSchemaGrammarDescription.reset();
            fSchemaGrammarDescription.setContextType(refType);
            fSchemaGrammarDescription.setBaseSystemId(doc2SystemId(schemaRoot));
            fSchemaGrammarDescription.setLocationHints(new String[] { schemaHint });
            fSchemaGrammarDescription.setTargetNamespace(callerTNS);
            boolean alreadyTraversed = false;
            XMLInputSource schemaSource = resolveSchemaSource(fSchemaGrammarDescription, mustResolve, child, true);
            if (fNamespaceGrowth && refType == XSDDescription.CONTEXT_INCLUDE) {
                try {
                    final String schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
                    alreadyTraversed = sg.getDocumentLocations().contains(schemaId);
                } catch (MalformedURIException e) {
                }
            }
            if (!alreadyTraversed) {
                newSchemaRoot = resolveSchema(schemaSource, fSchemaGrammarDescription, mustResolve, child);
                schemaNamespace = currSchemaInfo.fTargetNamespace;
            } else {
                fLastSchemaWasDuplicate = true;
            }
        } else {
            // no more possibility of schema references in well-formed  
            // schema...  
            break;
        }
        // If the schema is duplicate, we needn't call constructTrees() again.  
        // To handle mutual <include>s  
        XSDocumentInfo newSchemaInfo = null;
        if (fLastSchemaWasDuplicate) {
            newSchemaInfo = newSchemaRoot == null ? null : (XSDocumentInfo) fDoc2XSDocumentMap.get(newSchemaRoot);
        } else {
            newSchemaInfo = constructTrees(newSchemaRoot, schemaHint, fSchemaGrammarDescription, importCollision);
        }
        if (localName.equals(SchemaSymbols.ELT_REDEFINE) && newSchemaInfo != null) {
            // must record which schema we're redefining so that we can  
            // rename the right things later!  
            fRedefine2XSDMap.put(child, newSchemaInfo);
        }
        if (newSchemaRoot != null) {
            if (newSchemaInfo != null)
                dependencies.addElement(newSchemaInfo);
            newSchemaRoot = null;
        }
    }
    fDependencyMap.put(currSchemaInfo, dependencies);
    return currSchemaInfo;
}

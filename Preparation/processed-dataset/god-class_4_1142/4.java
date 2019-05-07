// end constructor  
/**
     * This method initiates the parse of a schema.  It will likely be
     * called from the Validator and it will make the
     * resulting grammar available; it returns a reference to this object just
     * in case.  A reset(XMLComponentManager) must be called before this methods is called.
     * @param is
     * @param desc
     * @param locationPairs
     * @return the SchemaGrammar
     * @throws IOException
     */
public SchemaGrammar parseSchema(XMLInputSource is, XSDDescription desc, Hashtable locationPairs) throws IOException {
    fLocationPairs = locationPairs;
    fSchemaParser.resetNodePool();
    SchemaGrammar grammar = null;
    String schemaNamespace = null;
    short referType = desc.getContextType();
    // if loading using JAXP schemaSource property, or using grammar caching loadGrammar  
    // the desc.targetNamespace is always null.  
    // Therefore we should not attempt to find out if  
    // the schema is already in the bucket, since in the case we have  
    // no namespace schema in the bucket, findGrammar will always return the  
    // no namespace schema.  
    if (referType != XSDDescription.CONTEXT_PREPARSE) {
        // first try to find it in the bucket/pool, return if one is found  
        if (fHonourAllSchemaLocations && referType == XSDDescription.CONTEXT_IMPORT && isExistingGrammar(desc, fNamespaceGrowth)) {
            grammar = fGrammarBucket.getGrammar(desc.getTargetNamespace());
        } else {
            grammar = findGrammar(desc, fNamespaceGrowth);
        }
        if (grammar != null) {
            if (!fNamespaceGrowth) {
                return grammar;
            } else {
                try {
                    if (grammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(is.getSystemId(), is.getBaseSystemId(), false))) {
                        return grammar;
                    }
                } catch (MalformedURIException e) {
                }
            }
        }
        schemaNamespace = desc.getTargetNamespace();
        // handle empty string URI as null  
        if (schemaNamespace != null) {
            schemaNamespace = fSymbolTable.addSymbol(schemaNamespace);
        }
    }
    // before parsing a schema, need to clear registries associated with  
    // parsing schemas  
    prepareForParse();
    Element schemaRoot = null;
    // first phase:  construct trees.  
    if (is instanceof DOMInputSource) {
        schemaRoot = getSchemaDocument(schemaNamespace, (DOMInputSource) is, referType == XSDDescription.CONTEXT_PREPARSE, referType, null);
    } else if (is instanceof SAXInputSource) {
        schemaRoot = getSchemaDocument(schemaNamespace, (SAXInputSource) is, referType == XSDDescription.CONTEXT_PREPARSE, referType, null);
    } else if (is instanceof StAXInputSource) {
        schemaRoot = getSchemaDocument(schemaNamespace, (StAXInputSource) is, referType == XSDDescription.CONTEXT_PREPARSE, referType, null);
    } else if (is instanceof XSInputSource) {
        schemaRoot = getSchemaDocument((XSInputSource) is, desc);
    } else {
        schemaRoot = getSchemaDocument(schemaNamespace, is, referType == XSDDescription.CONTEXT_PREPARSE, referType, null);
    }
    //is instanceof XMLInputSource  
    if (schemaRoot == null) {
        // something went wrong right off the hop  
        if (is instanceof XSInputSource) {
            return fGrammarBucket.getGrammar(desc.getTargetNamespace());
        }
        return grammar;
    }
    if (referType == XSDDescription.CONTEXT_PREPARSE) {
        Element schemaElem = schemaRoot;
        schemaNamespace = DOMUtil.getAttrValue(schemaElem, SchemaSymbols.ATT_TARGETNAMESPACE);
        if (schemaNamespace != null && schemaNamespace.length() > 0) {
            // Since now we've discovered a namespace, we need to update xsd key  
            // and store this schema in traversed schemas bucket  
            schemaNamespace = fSymbolTable.addSymbol(schemaNamespace);
            desc.setTargetNamespace(schemaNamespace);
        } else {
            schemaNamespace = null;
        }
        grammar = findGrammar(desc, fNamespaceGrowth);
        String schemaId = XMLEntityManager.expandSystemId(is.getSystemId(), is.getBaseSystemId(), false);
        if (grammar != null) {
            // When namespace growth is enabled and a null location is provided we cannot tell  
            // whether we've loaded this schema document before so we must assume that we haven't.  
            if (!fNamespaceGrowth || (schemaId != null && grammar.getDocumentLocations().contains(schemaId))) {
                return grammar;
            }
        }
        XSDKey key = new XSDKey(schemaId, referType, schemaNamespace);
        fTraversed.put(key, schemaRoot);
        if (schemaId != null) {
            fDoc2SystemId.put(schemaRoot, schemaId);
        }
    }
    // before constructing trees and traversing a schema, need to reset  
    // all traversers and clear all registries  
    prepareForTraverse();
    fRoot = constructTrees(schemaRoot, is.getSystemId(), desc, grammar != null);
    if (fRoot == null) {
        return null;
    }
    // second phase:  fill global registries.  
    buildGlobalNameRegistries();
    // third phase:  call traversers  
    ArrayList annotationInfo = fValidateAnnotations ? new ArrayList() : null;
    traverseSchemas(annotationInfo);
    // fourth phase: handle local element decls  
    traverseLocalElements();
    // fifth phase:  handle Keyrefs  
    resolveKeyRefs();
    // sixth phase:  validate attribute of non-schema namespaces  
    // REVISIT: skip this for now. we really don't want to do it.  
    //fAttributeChecker.checkNonSchemaAttributes(fGrammarBucket);  
    // seventh phase:  store imported grammars  
    // for all grammars with <import>s  
    for (int i = fAllTNSs.size() - 1; i >= 0; i--) {
        // get its target namespace  
        String tns = (String) fAllTNSs.elementAt(i);
        // get all namespaces it imports  
        Vector ins = (Vector) fImportMap.get(tns);
        // get the grammar  
        SchemaGrammar sg = fGrammarBucket.getGrammar(emptyString2Null(tns));
        if (sg == null)
            continue;
        SchemaGrammar isg;
        // for imported namespace  
        int count = 0;
        for (int j = 0; j < ins.size(); j++) {
            // get imported grammar  
            isg = fGrammarBucket.getGrammar((String) ins.elementAt(j));
            // reuse the same vector  
            if (isg != null)
                ins.setElementAt(isg, count++);
        }
        ins.setSize(count);
        // set the imported grammars  
        sg.setImportedGrammars(ins);
    }
    /** validate annotations **/
    if (fValidateAnnotations && annotationInfo.size() > 0) {
        validateAnnotations(annotationInfo);
    }
    // and return.  
    return fGrammarBucket.getGrammar(fRoot.fTargetNamespace);
}

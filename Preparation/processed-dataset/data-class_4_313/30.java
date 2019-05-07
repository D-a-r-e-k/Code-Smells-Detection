// since it is forbidden for traversers to talk to each other  
// directly (except wen a traverser encounters a local declaration),  
// this provides a generic means for a traverser to call  
// for the traversal of some declaration.  An XSDocumentInfo is  
// required because the XSDocumentInfo that the traverser is traversing  
// may bear no relation to the one the handler is operating on.  
// This method will:  
// 1.  See if a global definition matching declToTraverse exists;  
// 2. if so, determine if there is a path from currSchema to the  
// schema document where declToTraverse lives (i.e., do a lookup  
// in DependencyMap);  
// 3. depending on declType (which will be relevant to step 1 as  
// well), call the appropriate traverser with the appropriate  
// XSDocumentInfo object.  
// This method returns whatever the traverser it called returned;  
// this will be an Object of some kind  
// that lives in the Grammar.  
protected Object getGlobalDecl(XSDocumentInfo currSchema, int declType, QName declToTraverse, Element elmNode) {
    if (DEBUG_NODE_POOL) {
        System.out.println("TRAVERSE_GL: " + declToTraverse.toString());
    }
    // from the schema spec, all built-in types are present in all schemas,  
    // so if the requested component is a type, and could be found in the  
    // default schema grammar, we should return that type.  
    // otherwise (since we would support user-defined schema grammar) we'll  
    // use the normal way to get the decl  
    if (declToTraverse.uri != null && declToTraverse.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
        if (declType == TYPEDECL_TYPE) {
            Object retObj = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(declToTraverse.localpart);
            if (retObj != null)
                return retObj;
        }
    }
    // now check whether this document can access the requsted namespace  
    if (!currSchema.isAllowedNS(declToTraverse.uri)) {
        // cannot get to this schema from the one containing the requesting decl  
        if (currSchema.needReportTNSError(declToTraverse.uri)) {
            String code = declToTraverse.uri == null ? "src-resolve.4.1" : "src-resolve.4.2";
            reportSchemaError(code, new Object[] { fDoc2SystemId.get(currSchema.fSchemaElement), declToTraverse.uri, declToTraverse.rawname }, elmNode);
        }
    }
    // check whether there is grammar for the requested namespace  
    SchemaGrammar sGrammar = fGrammarBucket.getGrammar(declToTraverse.uri);
    if (sGrammar == null) {
        if (needReportTNSError(declToTraverse.uri))
            reportSchemaError("src-resolve", new Object[] { declToTraverse.rawname, COMP_TYPE[declType] }, elmNode);
        return null;
    }
    // if there is such grammar, check whether the requested component is in the grammar  
    Object retObj = getGlobalDeclFromGrammar(sGrammar, declType, declToTraverse.localpart);
    String declKey = declToTraverse.uri == null ? "," + declToTraverse.localpart : declToTraverse.uri + "," + declToTraverse.localpart;
    // if the component is parsed, return it  
    if (!fTolerateDuplicates) {
        if (retObj != null) {
            return retObj;
        }
    } else {
        Object retObj2 = getGlobalDecl(declKey, declType);
        if (retObj2 != null) {
            return retObj2;
        }
    }
    XSDocumentInfo schemaWithDecl = null;
    Element decl = null;
    XSDocumentInfo declDoc = null;
    // the component is not parsed, try to find a DOM element for it  
    switch(declType) {
        case ATTRIBUTE_TYPE:
            decl = (Element) fUnparsedAttributeRegistry.get(declKey);
            declDoc = (XSDocumentInfo) fUnparsedAttributeRegistrySub.get(declKey);
            break;
        case ATTRIBUTEGROUP_TYPE:
            decl = (Element) fUnparsedAttributeGroupRegistry.get(declKey);
            declDoc = (XSDocumentInfo) fUnparsedAttributeGroupRegistrySub.get(declKey);
            break;
        case ELEMENT_TYPE:
            decl = (Element) fUnparsedElementRegistry.get(declKey);
            declDoc = (XSDocumentInfo) fUnparsedElementRegistrySub.get(declKey);
            break;
        case GROUP_TYPE:
            decl = (Element) fUnparsedGroupRegistry.get(declKey);
            declDoc = (XSDocumentInfo) fUnparsedGroupRegistrySub.get(declKey);
            break;
        case IDENTITYCONSTRAINT_TYPE:
            decl = (Element) fUnparsedIdentityConstraintRegistry.get(declKey);
            declDoc = (XSDocumentInfo) fUnparsedIdentityConstraintRegistrySub.get(declKey);
            break;
        case NOTATION_TYPE:
            decl = (Element) fUnparsedNotationRegistry.get(declKey);
            declDoc = (XSDocumentInfo) fUnparsedNotationRegistrySub.get(declKey);
            break;
        case TYPEDECL_TYPE:
            decl = (Element) fUnparsedTypeRegistry.get(declKey);
            declDoc = (XSDocumentInfo) fUnparsedTypeRegistrySub.get(declKey);
            break;
        default:
            reportSchemaError("Internal-Error", new Object[] { "XSDHandler asked to locate component of type " + declType + "; it does not recognize this type!" }, elmNode);
    }
    // no DOM element found, so the component can't be located  
    if (decl == null) {
        if (retObj == null) {
            reportSchemaError("src-resolve", new Object[] { declToTraverse.rawname, COMP_TYPE[declType] }, elmNode);
        }
        return retObj;
    }
    // get the schema doc containing the component to be parsed  
    // it should always return non-null value, but since null-checking  
    // comes for free, let's be safe and check again  
    schemaWithDecl = findXSDocumentForDecl(currSchema, decl, declDoc);
    if (schemaWithDecl == null) {
        // cannot get to this schema from the one containing the requesting decl  
        if (retObj == null) {
            String code = declToTraverse.uri == null ? "src-resolve.4.1" : "src-resolve.4.2";
            reportSchemaError(code, new Object[] { fDoc2SystemId.get(currSchema.fSchemaElement), declToTraverse.uri, declToTraverse.rawname }, elmNode);
        }
        return retObj;
    }
    // a component is hidden, meaning either it's traversed, or being traversed.  
    // but we didn't find it in the grammar, so it's the latter case, and  
    // a circular reference. error!  
    if (DOMUtil.isHidden(decl, fHiddenNodes)) {
        if (retObj == null) {
            String code = CIRCULAR_CODES[declType];
            if (declType == TYPEDECL_TYPE) {
                if (SchemaSymbols.ELT_COMPLEXTYPE.equals(DOMUtil.getLocalName(decl))) {
                    code = "ct-props-correct.3";
                }
            }
            // decl must not be null if we're here...  
            reportSchemaError(code, new Object[] { declToTraverse.prefix + ":" + declToTraverse.localpart }, elmNode);
        }
        return retObj;
    }
    return traverseGlobalDecl(declType, decl, schemaWithDecl, sGrammar);
}

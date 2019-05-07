// handleIgnorableWhitespace(XMLString)  
/** Handle element. */
Augmentations handleStartElement(QName element, XMLAttributes attributes, Augmentations augs) {
    if (DEBUG) {
        System.out.println("==>handleStartElement: " + element);
    }
    // root element  
    if (fElementDepth == -1 && fValidationManager.isGrammarFound()) {
        if (fSchemaType == null) {
            // schemaType is not specified  
            // if a DTD grammar is found, we do the same thing as Dynamic:  
            // if a schema grammar is found, validation is performed;  
            // otherwise, skip the whole document.  
            fSchemaDynamicValidation = true;
        } else {
        }
    }
    // get xsi:schemaLocation and xsi:noNamespaceSchemaLocation attributes,  
    // parse them to get the grammars  
    String sLocation = attributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_SCHEMALOCATION);
    String nsLocation = attributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
    //store the location hints..  we need to do it so that we can defer the loading of grammar until  
    //there is a reference to a component from that namespace. To provide location hints to the  
    //application for a namespace  
    storeLocations(sLocation, nsLocation);
    // if we are in the content of "skip", then just skip this element  
    // REVISIT:  is this the correct behaviour for ID constraints?  -NG  
    if (fSkipValidationDepth >= 0) {
        fElementDepth++;
        if (fAugPSVI)
            augs = getEmptyAugs(augs);
        return augs;
    }
    // if we are not skipping this element, and there is a content model,  
    // we try to find the corresponding decl object for this element.  
    // the reason we move this part of code here is to make sure the  
    // error reported here (if any) is stored within the parent element's  
    // context, instead of that of the current element.  
    Object decl = null;
    if (fCurrentCM != null) {
        decl = fCurrentCM.oneTransition(element, fCurrCMState, fSubGroupHandler);
        // it could be an element decl or a wildcard decl  
        if (fCurrCMState[0] == XSCMValidator.FIRST_ERROR) {
            XSComplexTypeDecl ctype = (XSComplexTypeDecl) fCurrentType;
            //REVISIT: is it the only case we will have particle = null?  
            Vector next;
            if (ctype.fParticle != null && (next = fCurrentCM.whatCanGoHere(fCurrCMState)).size() > 0) {
                String expected = expectedStr(next);
                final int[] occurenceInfo = fCurrentCM.occurenceInfo(fCurrCMState);
                if (occurenceInfo != null) {
                    final int minOccurs = occurenceInfo[0];
                    final int maxOccurs = occurenceInfo[1];
                    final int count = occurenceInfo[2];
                    // Check if this is a violation of minOccurs  
                    if (count < minOccurs) {
                        final int required = minOccurs - count;
                        if (required > 1) {
                            reportSchemaError("cvc-complex-type.2.4.h", new Object[] { element.rawname, fCurrentCM.getTermName(occurenceInfo[3]), Integer.toString(minOccurs), Integer.toString(required) });
                        } else {
                            reportSchemaError("cvc-complex-type.2.4.g", new Object[] { element.rawname, fCurrentCM.getTermName(occurenceInfo[3]), Integer.toString(minOccurs) });
                        }
                    } else if (count >= maxOccurs && maxOccurs != SchemaSymbols.OCCURRENCE_UNBOUNDED) {
                        reportSchemaError("cvc-complex-type.2.4.e", new Object[] { element.rawname, expected, Integer.toString(maxOccurs) });
                    } else {
                        reportSchemaError("cvc-complex-type.2.4.a", new Object[] { element.rawname, expected });
                    }
                } else {
                    reportSchemaError("cvc-complex-type.2.4.a", new Object[] { element.rawname, expected });
                }
            } else {
                final int[] occurenceInfo = fCurrentCM.occurenceInfo(fCurrCMState);
                if (occurenceInfo != null) {
                    final int maxOccurs = occurenceInfo[1];
                    final int count = occurenceInfo[2];
                    // Check if this is a violation of maxOccurs  
                    if (count >= maxOccurs && maxOccurs != SchemaSymbols.OCCURRENCE_UNBOUNDED) {
                        reportSchemaError("cvc-complex-type.2.4.f", new Object[] { element.rawname, Integer.toString(maxOccurs) });
                    } else {
                        reportSchemaError("cvc-complex-type.2.4.d", new Object[] { element.rawname });
                    }
                } else {
                    reportSchemaError("cvc-complex-type.2.4.d", new Object[] { element.rawname });
                }
            }
        }
    }
    // if it's not the root element, we push the current states in the stacks  
    if (fElementDepth != -1) {
        ensureStackCapacity();
        fSubElementStack[fElementDepth] = true;
        fSubElement = false;
        fElemDeclStack[fElementDepth] = fCurrentElemDecl;
        fNilStack[fElementDepth] = fNil;
        fNotationStack[fElementDepth] = fNotation;
        fTypeStack[fElementDepth] = fCurrentType;
        fStrictAssessStack[fElementDepth] = fStrictAssess;
        fCMStack[fElementDepth] = fCurrentCM;
        fCMStateStack[fElementDepth] = fCurrCMState;
        fSawTextStack[fElementDepth] = fSawText;
        fStringContent[fElementDepth] = fSawCharacters;
    }
    // increase the element depth after we've saved  
    // all states for the parent element  
    fElementDepth++;
    fCurrentElemDecl = null;
    XSWildcardDecl wildcard = null;
    fCurrentType = null;
    fStrictAssess = true;
    fNil = false;
    fNotation = null;
    // and the buffer to hold the value of the element  
    fBuffer.setLength(0);
    fSawText = false;
    fSawCharacters = false;
    // check what kind of declaration the "decl" from  
    // oneTransition() maps to  
    if (decl != null) {
        if (decl instanceof XSElementDecl) {
            fCurrentElemDecl = (XSElementDecl) decl;
        } else {
            wildcard = (XSWildcardDecl) decl;
        }
    }
    // if the wildcard is skip, then return  
    if (wildcard != null && wildcard.fProcessContents == XSWildcardDecl.PC_SKIP) {
        fSkipValidationDepth = fElementDepth;
        if (fAugPSVI)
            augs = getEmptyAugs(augs);
        return augs;
    }
    if (fElementDepth == 0) {
        // 1.1.1.1 An element declaration was stipulated by the processor  
        if (fRootElementDeclaration != null) {
            fCurrentElemDecl = fRootElementDeclaration;
            checkElementMatchesRootElementDecl(fCurrentElemDecl, element);
        } else if (fRootElementDeclQName != null) {
            processRootElementDeclQName(fRootElementDeclQName, element);
        } else if (fRootTypeDefinition != null) {
            fCurrentType = fRootTypeDefinition;
        } else if (fRootTypeQName != null) {
            processRootTypeQName(fRootTypeQName);
        }
    }
    // if there was no processor stipulated type  
    if (fCurrentType == null) {
        // try again to get the element decl:  
        // case 1: find declaration for root element  
        // case 2: find declaration for element from another namespace  
        if (fCurrentElemDecl == null) {
            // try to find schema grammar by different means..  
            SchemaGrammar sGrammar = findSchemaGrammar(XSDDescription.CONTEXT_ELEMENT, element.uri, null, element, attributes);
            if (sGrammar != null) {
                fCurrentElemDecl = sGrammar.getGlobalElementDecl(element.localpart);
            }
        }
        if (fCurrentElemDecl != null) {
            // then get the type  
            fCurrentType = fCurrentElemDecl.fType;
        }
    }
    // check if we should be ignoring xsi:type on this element  
    if (fElementDepth == fIgnoreXSITypeDepth && fCurrentElemDecl == null) {
        fIgnoreXSITypeDepth++;
    }
    // process xsi:type attribute information  
    String xsiType = null;
    if (fElementDepth >= fIgnoreXSITypeDepth) {
        xsiType = attributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_TYPE);
    }
    // if no decl/type found for the current element  
    if (fCurrentType == null && xsiType == null) {
        // if this is the validation root, report an error, because  
        // we can't find eith decl or type for this element  
        // REVISIT: should we report error, or warning?  
        if (fElementDepth == 0) {
            // for dynamic validation, skip the whole content,  
            // because no grammar was found.  
            if (fDynamicValidation || fSchemaDynamicValidation) {
                // no schema grammar was found, but it's either dynamic  
                // validation, or another kind of grammar was found (DTD,  
                // for example). The intended behavior here is to skip  
                // the whole document. To improve performance, we try to  
                // remove the validator from the pipeline, since it's not  
                // supposed to do anything.  
                if (fDocumentSource != null) {
                    fDocumentSource.setDocumentHandler(fDocumentHandler);
                    if (fDocumentHandler != null)
                        fDocumentHandler.setDocumentSource(fDocumentSource);
                    // indicate that the validator was removed.  
                    fElementDepth = -2;
                    return augs;
                }
                fSkipValidationDepth = fElementDepth;
                if (fAugPSVI)
                    augs = getEmptyAugs(augs);
                return augs;
            }
            // We don't call reportSchemaError here, because the spec  
            // doesn't think it's invalid not to be able to find a  
            // declaration or type definition for an element. Xerces is  
            // reporting it as an error for historical reasons, but in  
            // PSVI, we shouldn't mark this element as invalid because  
            // of this. - SG  
            fXSIErrorReporter.fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "cvc-elt.1.a", new Object[] { element.rawname }, XMLErrorReporter.SEVERITY_ERROR);
        } else if (wildcard != null && wildcard.fProcessContents == XSWildcardDecl.PC_STRICT) {
            // report error, because wilcard = strict  
            reportSchemaError("cvc-complex-type.2.4.c", new Object[] { element.rawname });
        }
        // no element decl or type found for this element.  
        // Allowed by the spec, we can choose to either laxly assess this  
        // element, or to skip it. Now we choose lax assessment.  
        fCurrentType = SchemaGrammar.fAnyType;
        fStrictAssess = false;
        fNFullValidationDepth = fElementDepth;
        // any type has mixed content, so we don't need to append buffer  
        fAppendBuffer = false;
        // push error reporter context: record the current position  
        // This has to happen after we process skip contents,  
        // otherwise push and pop won't be correctly paired.  
        fXSIErrorReporter.pushContext();
    } else {
        // push error reporter context: record the current position  
        // This has to happen after we process skip contents,  
        // otherwise push and pop won't be correctly paired.  
        fXSIErrorReporter.pushContext();
        // get xsi:type  
        if (xsiType != null) {
            XSTypeDefinition oldType = fCurrentType;
            fCurrentType = getAndCheckXsiType(element, xsiType, attributes);
            // If it fails, use the old type. Use anyType if ther is no old type.  
            if (fCurrentType == null) {
                if (oldType == null)
                    fCurrentType = SchemaGrammar.fAnyType;
                else
                    fCurrentType = oldType;
            }
        }
        fNNoneValidationDepth = fElementDepth;
        // if the element has a fixed value constraint, we need to append  
        if (fCurrentElemDecl != null && fCurrentElemDecl.getConstraintType() == XSConstants.VC_FIXED) {
            fAppendBuffer = true;
        } else if (fCurrentType.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE) {
            fAppendBuffer = true;
        } else {
            // if the type is simple content complex type, we need to append  
            XSComplexTypeDecl ctype = (XSComplexTypeDecl) fCurrentType;
            fAppendBuffer = (ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_SIMPLE);
        }
    }
    // Element Locally Valid (Element)  
    // 2 Its {abstract} must be false.  
    if (fCurrentElemDecl != null && fCurrentElemDecl.getAbstract())
        reportSchemaError("cvc-elt.2", new Object[] { element.rawname });
    // make the current element validation root  
    if (fElementDepth == 0) {
        fValidationRoot = element.rawname;
    }
    // update normalization flags  
    if (fNormalizeData) {
        // reset values  
        fFirstChunk = true;
        fTrailing = false;
        fUnionType = false;
        fWhiteSpace = -1;
    }
    // Element Locally Valid (Type)  
    // 2 Its {abstract} must be false.  
    if (fCurrentType.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
        XSComplexTypeDecl ctype = (XSComplexTypeDecl) fCurrentType;
        if (ctype.getAbstract()) {
            reportSchemaError("cvc-type.2", new Object[] { element.rawname });
        }
        if (fNormalizeData) {
            // find out if the content type is simple and if variety is union  
            // to be able to do character normalization  
            if (ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_SIMPLE) {
                if (ctype.fXSSimpleType.getVariety() == XSSimpleType.VARIETY_UNION) {
                    fUnionType = true;
                } else {
                    try {
                        fWhiteSpace = ctype.fXSSimpleType.getWhitespace();
                    } catch (DatatypeException e) {
                    }
                }
            }
        }
    } else if (fNormalizeData) {
        // if !union type  
        XSSimpleType dv = (XSSimpleType) fCurrentType;
        if (dv.getVariety() == XSSimpleType.VARIETY_UNION) {
            fUnionType = true;
        } else {
            try {
                fWhiteSpace = dv.getWhitespace();
            } catch (DatatypeException e) {
            }
        }
    }
    // then try to get the content model  
    fCurrentCM = null;
    if (fCurrentType.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
        fCurrentCM = ((XSComplexTypeDecl) fCurrentType).getContentModel(fCMBuilder);
    }
    // and get the initial content model state  
    fCurrCMState = null;
    if (fCurrentCM != null)
        fCurrCMState = fCurrentCM.startContentModel();
    // get information about xsi:nil  
    String xsiNil = attributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_NIL);
    // only deal with xsi:nil when there is an element declaration  
    if (xsiNil != null && fCurrentElemDecl != null)
        fNil = getXsiNil(element, xsiNil);
    // now validate everything related with the attributes  
    // first, get the attribute group  
    XSAttributeGroupDecl attrGrp = null;
    if (fCurrentType.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
        XSComplexTypeDecl ctype = (XSComplexTypeDecl) fCurrentType;
        attrGrp = ctype.getAttrGrp();
    }
    if (fIDCChecking) {
        // activate identity constraints  
        fValueStoreCache.startElement();
        fMatcherStack.pushContext();
        //if (fCurrentElemDecl != null && fCurrentElemDecl.fIDCPos > 0 && !fIgnoreIDC) {  
        if (fCurrentElemDecl != null && fCurrentElemDecl.fIDCPos > 0) {
            fIdConstraint = true;
            // initialize when identity constrains are defined for the elem  
            fValueStoreCache.initValueStoresFor(fCurrentElemDecl, this);
        }
    }
    processAttributes(element, attributes, attrGrp);
    // add default attributes  
    if (attrGrp != null) {
        addDefaultAttributes(element, attributes, attrGrp);
    }
    // call all active identity constraints  
    int count = fMatcherStack.getMatcherCount();
    for (int i = 0; i < count; i++) {
        XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
        matcher.startElement(element, attributes);
    }
    if (fAugPSVI) {
        augs = getEmptyAugs(augs);
        // PSVI: add validation context  
        fCurrentPSVI.fValidationContext = fValidationRoot;
        // PSVI: add element declaration  
        fCurrentPSVI.fDeclaration = fCurrentElemDecl;
        // PSVI: add element type  
        fCurrentPSVI.fTypeDecl = fCurrentType;
        // PSVI: add notation attribute  
        fCurrentPSVI.fNotation = fNotation;
        // PSVI: add nil  
        fCurrentPSVI.fNil = fNil;
    }
    return augs;
}

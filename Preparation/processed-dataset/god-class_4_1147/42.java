// handleStartElement(QName,XMLAttributes,boolean)  
/**
     *  Handle end element. If there is not text content, and there is a
     *  {value constraint} on the corresponding element decl, then
     * set the fDefaultValue XMLString representing the default value.
     */
Augmentations handleEndElement(QName element, Augmentations augs) {
    if (DEBUG) {
        System.out.println("==>handleEndElement:" + element);
    }
    // if we are skipping, return  
    if (fSkipValidationDepth >= 0) {
        // but if this is the top element that we are skipping,  
        // restore the states.  
        if (fSkipValidationDepth == fElementDepth && fSkipValidationDepth > 0) {
            // set the partial validation depth to the depth of parent  
            fNFullValidationDepth = fSkipValidationDepth - 1;
            fSkipValidationDepth = -1;
            fElementDepth--;
            fSubElement = fSubElementStack[fElementDepth];
            fCurrentElemDecl = fElemDeclStack[fElementDepth];
            fNil = fNilStack[fElementDepth];
            fNotation = fNotationStack[fElementDepth];
            fCurrentType = fTypeStack[fElementDepth];
            fCurrentCM = fCMStack[fElementDepth];
            fStrictAssess = fStrictAssessStack[fElementDepth];
            fCurrCMState = fCMStateStack[fElementDepth];
            fSawText = fSawTextStack[fElementDepth];
            fSawCharacters = fStringContent[fElementDepth];
        } else {
            fElementDepth--;
        }
        // PSVI: validation attempted:  
        // use default values in psvi item for  
        // validation attempted, validity, and error codes  
        // check extra schema constraints on root element  
        if (fElementDepth == -1 && fFullChecking && !fUseGrammarPoolOnly) {
            XSConstraints.fullSchemaChecking(fGrammarBucket, fSubGroupHandler, fCMBuilder, fXSIErrorReporter.fErrorReporter);
        }
        if (fAugPSVI)
            augs = getEmptyAugs(augs);
        return augs;
    }
    // now validate the content of the element  
    processElementContent(element);
    if (fIDCChecking) {
        // Element Locally Valid (Element)  
        // 6 The element information item must be valid with respect to each of the {identity-constraint definitions} as per Identity-constraint Satisfied (3.11.4).  
        // call matchers and de-activate context  
        int oldCount = fMatcherStack.getMatcherCount();
        for (int i = oldCount - 1; i >= 0; i--) {
            XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
            if (fCurrentElemDecl == null) {
                matcher.endElement(element, fCurrentType, false, fValidatedInfo.actualValue, fValidatedInfo.actualValueType, fValidatedInfo.itemValueTypes);
            } else {
                matcher.endElement(element, fCurrentType, fCurrentElemDecl.getNillable(), fDefaultValue == null ? fValidatedInfo.actualValue : fCurrentElemDecl.fDefault.actualValue, fDefaultValue == null ? fValidatedInfo.actualValueType : fCurrentElemDecl.fDefault.actualValueType, fDefaultValue == null ? fValidatedInfo.itemValueTypes : fCurrentElemDecl.fDefault.itemValueTypes);
            }
        }
        if (fMatcherStack.size() > 0) {
            fMatcherStack.popContext();
        }
        int newCount = fMatcherStack.getMatcherCount();
        // handle everything *but* keyref's.  
        for (int i = oldCount - 1; i >= newCount; i--) {
            XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
            if (matcher instanceof Selector.Matcher) {
                Selector.Matcher selMatcher = (Selector.Matcher) matcher;
                IdentityConstraint id;
                if ((id = selMatcher.getIdentityConstraint()) != null && id.getCategory() != IdentityConstraint.IC_KEYREF) {
                    fValueStoreCache.transplant(id, selMatcher.getInitialDepth());
                }
            }
        }
        // now handle keyref's/...  
        for (int i = oldCount - 1; i >= newCount; i--) {
            XPathMatcher matcher = fMatcherStack.getMatcherAt(i);
            if (matcher instanceof Selector.Matcher) {
                Selector.Matcher selMatcher = (Selector.Matcher) matcher;
                IdentityConstraint id;
                if ((id = selMatcher.getIdentityConstraint()) != null && id.getCategory() == IdentityConstraint.IC_KEYREF) {
                    ValueStoreBase values = fValueStoreCache.getValueStoreFor(id, selMatcher.getInitialDepth());
                    if (values != null)
                        // nothing to do if nothing matched!  
                        values.endDocumentFragment();
                }
            }
        }
        fValueStoreCache.endElement();
    }
    // Check if we should modify the xsi:type ignore depth  
    // This check is independent of whether this is the validation root,  
    // and should be done before the element depth is decremented.  
    if (fElementDepth < fIgnoreXSITypeDepth) {
        fIgnoreXSITypeDepth--;
    }
    SchemaGrammar[] grammars = null;
    // have we reached the end tag of the validation root?  
    if (fElementDepth == 0) {
        // 7 If the element information item is the validation root, it must be valid per Validation Root Valid (ID/IDREF) (3.3.4).  
        String invIdRef = fValidationState.checkIDRefID();
        fValidationState.resetIDTables();
        if (invIdRef != null) {
            reportSchemaError("cvc-id.1", new Object[] { invIdRef });
        }
        // check extra schema constraints  
        if (fFullChecking && !fUseGrammarPoolOnly) {
            XSConstraints.fullSchemaChecking(fGrammarBucket, fSubGroupHandler, fCMBuilder, fXSIErrorReporter.fErrorReporter);
        }
        grammars = fGrammarBucket.getGrammars();
        // return the final set of grammars validator ended up with  
        if (fGrammarPool != null) {
            // Set grammars as immutable  
            for (int k = 0; k < grammars.length; k++) {
                grammars[k].setImmutable(true);
            }
            fGrammarPool.cacheGrammars(XMLGrammarDescription.XML_SCHEMA, grammars);
        }
        augs = endElementPSVI(true, grammars, augs);
    } else {
        augs = endElementPSVI(false, grammars, augs);
        // decrease element depth and restore states  
        fElementDepth--;
        // get the states for the parent element.  
        fSubElement = fSubElementStack[fElementDepth];
        fCurrentElemDecl = fElemDeclStack[fElementDepth];
        fNil = fNilStack[fElementDepth];
        fNotation = fNotationStack[fElementDepth];
        fCurrentType = fTypeStack[fElementDepth];
        fCurrentCM = fCMStack[fElementDepth];
        fStrictAssess = fStrictAssessStack[fElementDepth];
        fCurrCMState = fCMStateStack[fElementDepth];
        fSawText = fSawTextStack[fElementDepth];
        fSawCharacters = fStringContent[fElementDepth];
        // We should have a stack for whitespace value, and pop it up here.  
        // But when fWhiteSpace != -1, and we see a sub-element, it must be  
        // an error (at least for Schema 1.0). So for valid documents, the  
        // only value we are going to push/pop in the stack is -1.  
        // Here we just mimic the effect of popping -1. -SG  
        fWhiteSpace = -1;
        // Same for append buffer. Simple types and elements with fixed  
        // value constraint don't allow sub-elements. -SG  
        fAppendBuffer = false;
        // same here.  
        fUnionType = false;
    }
    return augs;
}

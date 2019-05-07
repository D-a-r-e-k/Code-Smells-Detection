// handleEndElement(QName,boolean)*/  
final Augmentations endElementPSVI(boolean root, SchemaGrammar[] grammars, Augmentations augs) {
    if (fAugPSVI) {
        augs = getEmptyAugs(augs);
        // the 5 properties sent on startElement calls  
        fCurrentPSVI.fDeclaration = this.fCurrentElemDecl;
        fCurrentPSVI.fTypeDecl = this.fCurrentType;
        fCurrentPSVI.fNotation = this.fNotation;
        fCurrentPSVI.fValidationContext = this.fValidationRoot;
        fCurrentPSVI.fNil = this.fNil;
        // PSVI: validation attempted  
        // nothing below or at the same level has none or partial  
        // (which means this level is strictly assessed, and all chidren  
        // are full), so this one has full  
        if (fElementDepth > fNFullValidationDepth) {
            fCurrentPSVI.fValidationAttempted = ElementPSVI.VALIDATION_FULL;
        } else if (fElementDepth > fNNoneValidationDepth) {
            fCurrentPSVI.fValidationAttempted = ElementPSVI.VALIDATION_NONE;
        } else {
            fCurrentPSVI.fValidationAttempted = ElementPSVI.VALIDATION_PARTIAL;
        }
        // this guarantees that depth settings do not cross-over between sibling nodes  
        if (fNFullValidationDepth == fElementDepth) {
            fNFullValidationDepth = fElementDepth - 1;
        }
        if (fNNoneValidationDepth == fElementDepth) {
            fNNoneValidationDepth = fElementDepth - 1;
        }
        if (fDefaultValue != null)
            fCurrentPSVI.fSpecified = true;
        fCurrentPSVI.fMemberType = fValidatedInfo.memberType;
        fCurrentPSVI.fNormalizedValue = fValidatedInfo.normalizedValue;
        fCurrentPSVI.fActualValue = fValidatedInfo.actualValue;
        fCurrentPSVI.fActualValueType = fValidatedInfo.actualValueType;
        fCurrentPSVI.fItemValueTypes = fValidatedInfo.itemValueTypes;
        if (fStrictAssess) {
            // get all errors for the current element, its attribute,  
            // and subelements (if they were strictly assessed).  
            // any error would make this element invalid.  
            // and we merge these errors to the parent element.  
            String[] errors = fXSIErrorReporter.mergeContext();
            // PSVI: error codes  
            fCurrentPSVI.fErrors = errors;
            // PSVI: validity  
            fCurrentPSVI.fValidity = (errors == null) ? ElementPSVI.VALIDITY_VALID : ElementPSVI.VALIDITY_INVALID;
        } else {
            // PSVI: validity  
            fCurrentPSVI.fValidity = ElementPSVI.VALIDITY_NOTKNOWN;
            // Discard the current context: ignore any error happened within  
            // the sub-elements/attributes of this element, because those  
            // errors won't affect the validity of the parent elements.  
            fXSIErrorReporter.popContext();
        }
        if (root) {
            // store [schema information] in the PSVI  
            fCurrentPSVI.fGrammars = grammars;
            fCurrentPSVI.fSchemaInformation = null;
        }
    }
    return augs;
}

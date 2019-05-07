//processAttributes  
void processOneAttribute(QName element, XMLAttributes attributes, int index, XSAttributeDecl currDecl, XSAttributeUseImpl currUse, AttributePSVImpl attrPSVI) {
    String attrValue = attributes.getValue(index);
    fXSIErrorReporter.pushContext();
    // Attribute Locally Valid  
    // For an attribute information item to be locally valid with respect to an attribute declaration all of the following must be true:  
    // 1 The declaration must not be absent (see Missing Sub-components (5.3) for how this can fail to be the case).  
    // 2 Its {type definition} must not be absent.  
    // 3 The item's normalized value must be locally valid with respect to that {type definition} as per String Valid (3.14.4).  
    // get simple type  
    XSSimpleType attDV = currDecl.fType;
    Object actualValue = null;
    try {
        actualValue = attDV.validate(attrValue, fValidationState, fValidatedInfo);
        // store the normalized value  
        if (fNormalizeData) {
            attributes.setValue(index, fValidatedInfo.normalizedValue);
        }
        // PSVI: element notation  
        if (attDV.getVariety() == XSSimpleType.VARIETY_ATOMIC && attDV.getPrimitiveKind() == XSSimpleType.PRIMITIVE_NOTATION) {
            QName qName = (QName) actualValue;
            SchemaGrammar grammar = fGrammarBucket.getGrammar(qName.uri);
            //REVISIT: is it possible for the notation to be in different namespace than the attribute  
            //with which it is associated, CHECK !!  <fof n1:att1 = "n2:notation1" ..>  
            // should we give chance to the application to be able to  retrieve a grammar - nb  
            //REVISIT: what would be the triggering component here.. if it is attribute value that  
            // triggered the loading of grammar ?? -nb  
            if (grammar != null) {
                fNotation = grammar.getGlobalNotationDecl(qName.localpart);
            }
        }
    } catch (InvalidDatatypeValueException idve) {
        reportSchemaError(idve.getKey(), idve.getArgs());
        reportSchemaError("cvc-attribute.3", new Object[] { element.rawname, fTempQName.rawname, attrValue, (attDV instanceof XSSimpleTypeDecl) ? ((XSSimpleTypeDecl) attDV).getTypeName() : attDV.getName() });
    }
    // get the value constraint from use or decl  
    // 4 The item's actual value must match the value of the {value constraint}, if it is present and fixed.                 // now check the value against the simpleType  
    if (actualValue != null && currDecl.getConstraintType() == XSConstants.VC_FIXED) {
        if (!ValidatedInfo.isComparable(fValidatedInfo, currDecl.fDefault) || !actualValue.equals(currDecl.fDefault.actualValue)) {
            reportSchemaError("cvc-attribute.4", new Object[] { element.rawname, fTempQName.rawname, attrValue, currDecl.fDefault.stringValue() });
        }
    }
    // 3.1 If there is among the {attribute uses} an attribute use with an {attribute declaration} whose {name} matches the attribute information item's [local name] and whose {target namespace} is identical to the attribute information item's [namespace name] (where an absent {target namespace} is taken to be identical to a [namespace name] with no value), then the attribute information must be valid with respect to that attribute use as per Attribute Locally Valid (Use) (3.5.4). In this case the {attribute declaration} of that attribute use is the context-determined declaration for the attribute information item with respect to Schema-Validity Assessment (Attribute) (3.2.4) and Assessment Outcome (Attribute) (3.2.5).  
    if (actualValue != null && currUse != null && currUse.fConstraintType == XSConstants.VC_FIXED) {
        if (!ValidatedInfo.isComparable(fValidatedInfo, currUse.fDefault) || !actualValue.equals(currUse.fDefault.actualValue)) {
            reportSchemaError("cvc-complex-type.3.1", new Object[] { element.rawname, fTempQName.rawname, attrValue, currUse.fDefault.stringValue() });
        }
    }
    if (fIdConstraint) {
        attrPSVI.fActualValue = actualValue;
    }
    if (fAugPSVI) {
        // PSVI: attribute declaration  
        attrPSVI.fDeclaration = currDecl;
        // PSVI: attribute type  
        attrPSVI.fTypeDecl = attDV;
        // PSVI: attribute memberType  
        attrPSVI.fMemberType = fValidatedInfo.memberType;
        // PSVI: attribute normalized value  
        // NOTE: we always store the normalized value, even if it's invlid,  
        // because it might still be useful to the user. But when the it's  
        // not valid, the normalized value is not trustable.  
        attrPSVI.fNormalizedValue = fValidatedInfo.normalizedValue;
        attrPSVI.fActualValue = fValidatedInfo.actualValue;
        attrPSVI.fActualValueType = fValidatedInfo.actualValueType;
        attrPSVI.fItemValueTypes = fValidatedInfo.itemValueTypes;
        // PSVI: validation attempted:  
        attrPSVI.fValidationAttempted = AttributePSVI.VALIDATION_FULL;
        // We have seen an attribute that was declared.  
        fNNoneValidationDepth = fElementDepth;
        String[] errors = fXSIErrorReporter.mergeContext();
        // PSVI: error codes  
        attrPSVI.fErrors = errors;
        // PSVI: validity  
        attrPSVI.fValidity = (errors == null) ? AttributePSVI.VALIDITY_VALID : AttributePSVI.VALIDITY_INVALID;
    }
}

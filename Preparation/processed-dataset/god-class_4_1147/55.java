// addDefaultAttributes  
/**
     *  If there is not text content, and there is a
     *  {value constraint} on the corresponding element decl, then return
     *  an XMLString representing the default value.
     */
void processElementContent(QName element) {
    // 1 If the item is ?valid? with respect to an element declaration as per Element Locally Valid (Element) (?3.3.4) and the {value constraint} is present, but clause 3.2 of Element Locally Valid (Element) (?3.3.4) above is not satisfied and the item has no element or character information item [children], then schema. Furthermore, the post-schema-validation infoset has the canonical lexical representation of the {value constraint} value as the item's [schema normalized value] property.  
    if (fCurrentElemDecl != null && fCurrentElemDecl.fDefault != null && !fSawText && !fSubElement && !fNil) {
        String strv = fCurrentElemDecl.fDefault.stringValue();
        int bufLen = strv.length();
        if (fNormalizedStr.ch == null || fNormalizedStr.ch.length < bufLen) {
            fNormalizedStr.ch = new char[bufLen];
        }
        strv.getChars(0, bufLen, fNormalizedStr.ch, 0);
        fNormalizedStr.offset = 0;
        fNormalizedStr.length = bufLen;
        fDefaultValue = fNormalizedStr;
    }
    // fixed values are handled later, after xsi:type determined.  
    fValidatedInfo.normalizedValue = null;
    // Element Locally Valid (Element)  
    // 3.2.1 The element information item must have no character or element information item [children].  
    if (fNil) {
        if (fSubElement || fSawText) {
            reportSchemaError("cvc-elt.3.2.1", new Object[] { element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL });
        }
    }
    this.fValidatedInfo.reset();
    // 5 The appropriate case among the following must be true:  
    // 5.1 If the declaration has a {value constraint}, the item has neither element nor character [children] and clause 3.2 has not applied, then all of the following must be true:  
    if (fCurrentElemDecl != null && fCurrentElemDecl.getConstraintType() != XSConstants.VC_NONE && !fSubElement && !fSawText && !fNil) {
        // 5.1.1 If the actual type definition is a local type definition then the canonical lexical representation of the {value constraint} value must be a valid default for the actual type definition as defined in Element Default Valid (Immediate) (3.3.6).  
        if (fCurrentType != fCurrentElemDecl.fType) {
            //REVISIT:we should pass ValidatedInfo here.  
            if (XSConstraints.ElementDefaultValidImmediate(fCurrentType, fCurrentElemDecl.fDefault.stringValue(), fState4XsiType, null) == null)
                reportSchemaError("cvc-elt.5.1.1", new Object[] { element.rawname, fCurrentType.getName(), fCurrentElemDecl.fDefault.stringValue() });
        }
        // 5.1.2 The element information item with the canonical lexical representation of the {value constraint} value used as its normalized value must be valid with respect to the actual type definition as defined by Element Locally Valid (Type) (3.3.4).  
        // REVISIT: don't use toString, but validateActualValue instead  
        //          use the fState4ApplyDefault  
        elementLocallyValidType(element, fCurrentElemDecl.fDefault.stringValue());
    } else {
        // The following method call also deal with clause 1.2.2 of the constraint  
        // Validation Rule: Schema-Validity Assessment (Element)  
        // 5.2 If the declaration has no {value constraint} or the item has either element or character [children] or clause 3.2 has applied, then all of the following must be true:  
        // 5.2.1 The element information item must be valid with respect to the actual type definition as defined by Element Locally Valid (Type) (3.3.4).  
        Object actualValue = elementLocallyValidType(element, fBuffer);
        // 5.2.2 If there is a fixed {value constraint} and clause 3.2 has not applied, all of the following must be true:  
        if (fCurrentElemDecl != null && fCurrentElemDecl.getConstraintType() == XSConstants.VC_FIXED && !fNil) {
            String content = fBuffer.toString();
            // 5.2.2.1 The element information item must have no element information item [children].  
            if (fSubElement)
                reportSchemaError("cvc-elt.5.2.2.1", new Object[] { element.rawname });
            // 5.2.2.2 The appropriate case among the following must be true:  
            if (fCurrentType.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
                XSComplexTypeDecl ctype = (XSComplexTypeDecl) fCurrentType;
                // 5.2.2.2.1 If the {content type} of the actual type definition is mixed, then the initial value of the item must match the canonical lexical representation of the {value constraint} value.  
                if (ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_MIXED) {
                    // REVISIT: how to get the initial value, does whiteSpace count?  
                    if (!fCurrentElemDecl.fDefault.normalizedValue.equals(content))
                        reportSchemaError("cvc-elt.5.2.2.2.1", new Object[] { element.rawname, content, fCurrentElemDecl.fDefault.normalizedValue });
                } else if (ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_SIMPLE) {
                    if (actualValue != null && (!ValidatedInfo.isComparable(fValidatedInfo, fCurrentElemDecl.fDefault) || !actualValue.equals(fCurrentElemDecl.fDefault.actualValue))) {
                        reportSchemaError("cvc-elt.5.2.2.2.2", new Object[] { element.rawname, content, fCurrentElemDecl.fDefault.stringValue() });
                    }
                }
            } else if (fCurrentType.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE) {
                if (actualValue != null && (!ValidatedInfo.isComparable(fValidatedInfo, fCurrentElemDecl.fDefault) || !actualValue.equals(fCurrentElemDecl.fDefault.actualValue))) {
                    // REVISIT: the spec didn't mention this case: fixed  
                    //          value with simple type  
                    reportSchemaError("cvc-elt.5.2.2.2.2", new Object[] { element.rawname, content, fCurrentElemDecl.fDefault.stringValue() });
                }
            }
        }
    }
    if (fDefaultValue == null && fNormalizeData && fDocumentHandler != null && fUnionType) {
        // for union types we need to send data because we delayed sending  
        // this data when we received it in the characters() call.  
        String content = fValidatedInfo.normalizedValue;
        if (content == null)
            content = fBuffer.toString();
        int bufLen = content.length();
        if (fNormalizedStr.ch == null || fNormalizedStr.ch.length < bufLen) {
            fNormalizedStr.ch = new char[bufLen];
        }
        content.getChars(0, bufLen, fNormalizedStr.ch, 0);
        fNormalizedStr.offset = 0;
        fNormalizedStr.length = bufLen;
        fDocumentHandler.characters(fNormalizedStr, null);
    }
}

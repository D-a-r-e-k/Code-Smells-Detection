// elementLocallyValidType  
Object elementLocallyValidComplexType(QName element, Object textContent) {
    Object actualValue = null;
    XSComplexTypeDecl ctype = (XSComplexTypeDecl) fCurrentType;
    // Element Locally Valid (Complex Type)  
    // For an element information item to be locally valid with respect to a complex type definition all of the following must be true:  
    // 1 {abstract} is false.  
    // 2 If clause 3.2 of Element Locally Valid (Element) (3.3.4) did not apply, then the appropriate case among the following must be true:  
    if (!fNil) {
        // 2.1 If the {content type} is empty, then the element information item has no character or element information item [children].  
        if (ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_EMPTY && (fSubElement || fSawText)) {
            reportSchemaError("cvc-complex-type.2.1", new Object[] { element.rawname });
        } else if (ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_SIMPLE) {
            if (fSubElement)
                reportSchemaError("cvc-complex-type.2.2", new Object[] { element.rawname });
            XSSimpleType dv = ctype.fXSSimpleType;
            try {
                if (!fNormalizeData || fUnionType) {
                    fValidationState.setNormalizationRequired(true);
                }
                actualValue = dv.validate(textContent, fValidationState, fValidatedInfo);
            } catch (InvalidDatatypeValueException e) {
                reportSchemaError(e.getKey(), e.getArgs());
                reportSchemaError("cvc-complex-type.2.2", new Object[] { element.rawname });
            }
        } else if (ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_ELEMENT) {
            if (fSawCharacters) {
                reportSchemaError("cvc-complex-type.2.3", new Object[] { element.rawname });
            }
        }
        // 2.4 If the {content type} is element-only or mixed, then the sequence of the element information item's element information item [children], if any, taken in order, is valid with respect to the {content type}'s particle, as defined in Element Sequence Locally Valid (Particle) (3.9.4).  
        if (ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_ELEMENT || ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_MIXED) {
            // if the current state is a valid state, check whether  
            // it's one of the final states.  
            if (DEBUG) {
                System.out.println(fCurrCMState);
            }
            if (fCurrCMState[0] >= 0 && !fCurrentCM.endContentModel(fCurrCMState)) {
                String expected = expectedStr(fCurrentCM.whatCanGoHere(fCurrCMState));
                final int[] occurenceInfo = fCurrentCM.occurenceInfo(fCurrCMState);
                if (occurenceInfo != null) {
                    final int minOccurs = occurenceInfo[0];
                    final int count = occurenceInfo[2];
                    // Check if this is a violation of minOccurs  
                    if (count < minOccurs) {
                        final int required = minOccurs - count;
                        if (required > 1) {
                            reportSchemaError("cvc-complex-type.2.4.j", new Object[] { element.rawname, fCurrentCM.getTermName(occurenceInfo[3]), Integer.toString(minOccurs), Integer.toString(required) });
                        } else {
                            reportSchemaError("cvc-complex-type.2.4.i", new Object[] { element.rawname, fCurrentCM.getTermName(occurenceInfo[3]), Integer.toString(minOccurs) });
                        }
                    } else {
                        reportSchemaError("cvc-complex-type.2.4.b", new Object[] { element.rawname, expected });
                    }
                } else {
                    reportSchemaError("cvc-complex-type.2.4.b", new Object[] { element.rawname, expected });
                }
            }
        }
    }
    return actualValue;
}

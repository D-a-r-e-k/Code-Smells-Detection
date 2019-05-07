// processElementContent  
Object elementLocallyValidType(QName element, Object textContent) {
    if (fCurrentType == null)
        return null;
    Object retValue = null;
    // Element Locally Valid (Type)  
    // 3 The appropriate case among the following must be true:  
    // 3.1 If the type definition is a simple type definition, then all of the following must be true:  
    if (fCurrentType.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE) {
        // 3.1.2 The element information item must have no element information item [children].  
        if (fSubElement)
            reportSchemaError("cvc-type.3.1.2", new Object[] { element.rawname });
        // 3.1.3 If clause 3.2 of Element Locally Valid (Element) (3.3.4) did not apply, then the normalized value must be valid with respect to the type definition as defined by String Valid (3.14.4).  
        if (!fNil) {
            XSSimpleType dv = (XSSimpleType) fCurrentType;
            try {
                if (!fNormalizeData || fUnionType) {
                    fValidationState.setNormalizationRequired(true);
                }
                retValue = dv.validate(textContent, fValidationState, fValidatedInfo);
            } catch (InvalidDatatypeValueException e) {
                reportSchemaError(e.getKey(), e.getArgs());
                reportSchemaError("cvc-type.3.1.3", new Object[] { element.rawname, textContent });
            }
        }
    } else {
        // 3.2 If the type definition is a complex type definition, then the element information item must be valid with respect to the type definition as per Element Locally Valid (Complex Type) (3.4.4);  
        retValue = elementLocallyValidComplexType(element, textContent);
    }
    return retValue;
}

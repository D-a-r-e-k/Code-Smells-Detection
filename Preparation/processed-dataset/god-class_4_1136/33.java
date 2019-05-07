// isExternalEntityRefInAttrValue(String):String  
/**
     * Validate attributes in DTD fashion.
     */
protected void validateDTDattribute(QName element, String attValue, XMLAttributeDecl attributeDecl) throws XNIException {
    switch(attributeDecl.simpleType.type) {
        case XMLSimpleType.TYPE_ENTITY:
            {
                // NOTE: Save this information because invalidStandaloneAttDef  
                boolean isAlistAttribute = attributeDecl.simpleType.list;
                try {
                    if (isAlistAttribute) {
                        fValENTITIES.validate(attValue, fValidationState);
                    } else {
                        fValENTITY.validate(attValue, fValidationState);
                    }
                } catch (InvalidDatatypeValueException ex) {
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, ex.getKey(), ex.getArgs(), XMLErrorReporter.SEVERITY_ERROR);
                }
                break;
            }
        case XMLSimpleType.TYPE_NOTATION:
        case XMLSimpleType.TYPE_ENUMERATION:
            {
                boolean found = false;
                String[] enumVals = attributeDecl.simpleType.enumeration;
                if (enumVals == null) {
                    found = false;
                } else
                    for (int i = 0; i < enumVals.length; i++) {
                        if (attValue == enumVals[i] || attValue.equals(enumVals[i])) {
                            found = true;
                            break;
                        }
                    }
                if (!found) {
                    StringBuffer enumValueString = new StringBuffer();
                    if (enumVals != null)
                        for (int i = 0; i < enumVals.length; i++) {
                            enumValueString.append(enumVals[i] + " ");
                        }
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_ATTRIBUTE_VALUE_NOT_IN_LIST", new Object[] { attributeDecl.name.rawname, attValue, enumValueString }, XMLErrorReporter.SEVERITY_ERROR);
                }
                break;
            }
        case XMLSimpleType.TYPE_ID:
            {
                try {
                    fValID.validate(attValue, fValidationState);
                } catch (InvalidDatatypeValueException ex) {
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, ex.getKey(), ex.getArgs(), XMLErrorReporter.SEVERITY_ERROR);
                }
                break;
            }
        case XMLSimpleType.TYPE_IDREF:
            {
                boolean isAlistAttribute = attributeDecl.simpleType.list;
                //Caveat - Save this information because invalidStandaloneAttDef  
                try {
                    if (isAlistAttribute) {
                        fValIDRefs.validate(attValue, fValidationState);
                    } else {
                        fValIDRef.validate(attValue, fValidationState);
                    }
                } catch (InvalidDatatypeValueException ex) {
                    if (isAlistAttribute) {
                        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "IDREFSInvalid", new Object[] { attValue }, XMLErrorReporter.SEVERITY_ERROR);
                    } else {
                        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, ex.getKey(), ex.getArgs(), XMLErrorReporter.SEVERITY_ERROR);
                    }
                }
                break;
            }
        case XMLSimpleType.TYPE_NMTOKEN:
            {
                boolean isAlistAttribute = attributeDecl.simpleType.list;
                //Caveat - Save this information because invalidStandaloneAttDef  
                //changes fTempAttDef  
                try {
                    if (isAlistAttribute) {
                        fValNMTOKENS.validate(attValue, fValidationState);
                    } else {
                        fValNMTOKEN.validate(attValue, fValidationState);
                    }
                } catch (InvalidDatatypeValueException ex) {
                    if (isAlistAttribute) {
                        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "NMTOKENSInvalid", new Object[] { attValue }, XMLErrorReporter.SEVERITY_ERROR);
                    } else {
                        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "NMTOKENInvalid", new Object[] { attValue }, XMLErrorReporter.SEVERITY_ERROR);
                    }
                }
                break;
            }
    }
}

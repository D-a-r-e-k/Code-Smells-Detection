// startAttlist(String)  
/**
     * An attribute declaration.
     * 
     * @param elementName   The name of the element that this attribute
     *                      is associated with.
     * @param attributeName The name of the attribute.
     * @param type          The attribute type. This value will be one of
     *                      the following: "CDATA", "ENTITY", "ENTITIES",
     *                      "ENUMERATION", "ID", "IDREF", "IDREFS", 
     *                      "NMTOKEN", "NMTOKENS", or "NOTATION".
     * @param enumeration   If the type has the value "ENUMERATION" or
     *                      "NOTATION", this array holds the allowed attribute
     *                      values; otherwise, this array is null.
     * @param defaultType   The attribute default type. This value will be
     *                      one of the following: "#FIXED", "#IMPLIED",
     *                      "#REQUIRED", or null.
     * @param defaultValue  The attribute default value, or null if no
     *                      default value is specified.
     * @param nonNormalizedDefaultValue  The attribute default value with no normalization 
     *                      performed, or null if no default value is specified.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs) throws XNIException {
    if (type != XMLSymbols.fCDATASymbol && defaultValue != null) {
        normalizeDefaultAttrValue(defaultValue);
    }
    if (fValidation) {
        boolean duplicateAttributeDef = false;
        //Get Grammar index to grammar array  
        DTDGrammar grammar = (fDTDGrammar != null ? fDTDGrammar : fGrammarBucket.getActiveGrammar());
        int elementIndex = grammar.getElementDeclIndex(elementName);
        if (grammar.getAttributeDeclIndex(elementIndex, attributeName) != -1) {
            //more than one attribute definition is provided for the same attribute of a given element type.  
            duplicateAttributeDef = true;
            //this feature works only when validation is true.  
            if (fWarnDuplicateAttdef) {
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_DUPLICATE_ATTRIBUTE_DEFINITION", new Object[] { elementName, attributeName }, XMLErrorReporter.SEVERITY_WARNING);
            }
        }
        //  
        // a) VC: One ID per Element Type, If duplicate ID attribute  
        // b) VC: ID attribute Default. if there is a declareared attribute  
        //        default for ID it should be of type #IMPLIED or #REQUIRED  
        if (type == XMLSymbols.fIDSymbol) {
            if (defaultValue != null && defaultValue.length != 0) {
                if (defaultType == null || !(defaultType == XMLSymbols.fIMPLIEDSymbol || defaultType == XMLSymbols.fREQUIREDSymbol)) {
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "IDDefaultTypeInvalid", new Object[] { attributeName }, XMLErrorReporter.SEVERITY_ERROR);
                }
            }
            if (!fTableOfIDAttributeNames.containsKey(elementName)) {
                fTableOfIDAttributeNames.put(elementName, attributeName);
            } else {
                //we should not report an error, when there is duplicate attribute definition for given element type  
                //according to XML 1.0 spec, When more than one definition is provided for the same attribute of a given  
                //element type, the first declaration is binding and later declaration are *ignored*. So processor should   
                //ignore the second declarations, however an application would be warned of the duplicate attribute defintion   
                // if http://apache.org/xml/features/validation/warn-on-duplicate-attdef feature is set to true,  
                // one typical case where this could be a  problem, when any XML file    
                // provide the ID type information through internal subset so that it is available to the parser which read   
                //only internal subset. Now that attribute declaration(ID Type) can again be part of external parsed entity   
                //referenced. At that time if parser doesn't make this distinction it will throw an error for VC One ID per   
                //Element Type, which (second defintion) actually should be ignored. Application behavior may differ on the  
                //basis of error or warning thrown. - nb.  
                if (!duplicateAttributeDef) {
                    String previousIDAttributeName = (String) fTableOfIDAttributeNames.get(elementName);
                    //rule a)  
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_MORE_THAN_ONE_ID_ATTRIBUTE", new Object[] { elementName, previousIDAttributeName, attributeName }, XMLErrorReporter.SEVERITY_ERROR);
                }
            }
        }
        //  
        //  VC: One Notation Per Element Type, should check if there is a  
        //      duplicate NOTATION attribute  
        if (type == XMLSymbols.fNOTATIONSymbol) {
            // VC: Notation Attributes: all notation names in the  
            //     (attribute) declaration must be declared.  
            for (int i = 0; i < enumeration.length; i++) {
                fNotationEnumVals.put(enumeration[i], attributeName);
            }
            if (fTableOfNOTATIONAttributeNames.containsKey(elementName) == false) {
                fTableOfNOTATIONAttributeNames.put(elementName, attributeName);
            } else {
                //we should not report an error, when there is duplicate attribute definition for given element type  
                //according to XML 1.0 spec, When more than one definition is provided for the same attribute of a given  
                //element type, the first declaration is binding and later declaration are *ignored*. So processor should   
                //ignore the second declarations, however an application would be warned of the duplicate attribute definition   
                // if http://apache.org/xml/features/validation/warn-on-duplicate-attdef feature is set to true, Application behavior may differ on the basis of error or   
                //warning thrown. - nb.  
                if (!duplicateAttributeDef) {
                    String previousNOTATIONAttributeName = (String) fTableOfNOTATIONAttributeNames.get(elementName);
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_MORE_THAN_ONE_NOTATION_ATTRIBUTE", new Object[] { elementName, previousNOTATIONAttributeName, attributeName }, XMLErrorReporter.SEVERITY_ERROR);
                }
            }
        }
        // VC: No Duplicate Tokens  
        // XML 1.0 SE Errata - E2  
        if (type == XMLSymbols.fENUMERATIONSymbol || type == XMLSymbols.fNOTATIONSymbol) {
            outer: for (int i = 0; i < enumeration.length; ++i) {
                for (int j = i + 1; j < enumeration.length; ++j) {
                    if (enumeration[i].equals(enumeration[j])) {
                        // Only report the first uniqueness violation. There could be others,  
                        // but additional overhead would be incurred tracking unique tokens  
                        // that have already been encountered. -- mrglavas  
                        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, type == XMLSymbols.fENUMERATIONSymbol ? "MSG_DISTINCT_TOKENS_IN_ENUMERATION" : "MSG_DISTINCT_NOTATION_IN_ENUMERATION", new Object[] { elementName, enumeration[i], attributeName }, XMLErrorReporter.SEVERITY_ERROR);
                        break outer;
                    }
                }
            }
        }
        // VC: Attribute Default Legal  
        boolean ok = true;
        if (defaultValue != null && (defaultType == null || (defaultType != null && defaultType == XMLSymbols.fFIXEDSymbol))) {
            String value = defaultValue.toString();
            if (type == XMLSymbols.fNMTOKENSSymbol || type == XMLSymbols.fENTITIESSymbol || type == XMLSymbols.fIDREFSSymbol) {
                StringTokenizer tokenizer = new StringTokenizer(value, " ");
                if (tokenizer.hasMoreTokens()) {
                    while (true) {
                        String nmtoken = tokenizer.nextToken();
                        if (type == XMLSymbols.fNMTOKENSSymbol) {
                            if (!isValidNmtoken(nmtoken)) {
                                ok = false;
                                break;
                            }
                        } else if (type == XMLSymbols.fENTITIESSymbol || type == XMLSymbols.fIDREFSSymbol) {
                            if (!isValidName(nmtoken)) {
                                ok = false;
                                break;
                            }
                        }
                        if (!tokenizer.hasMoreTokens()) {
                            break;
                        }
                    }
                }
            } else {
                if (type == XMLSymbols.fENTITYSymbol || type == XMLSymbols.fIDSymbol || type == XMLSymbols.fIDREFSymbol || type == XMLSymbols.fNOTATIONSymbol) {
                    if (!isValidName(value)) {
                        ok = false;
                    }
                } else if (type == XMLSymbols.fNMTOKENSymbol || type == XMLSymbols.fENUMERATIONSymbol) {
                    if (!isValidNmtoken(value)) {
                        ok = false;
                    }
                }
                if (type == XMLSymbols.fNOTATIONSymbol || type == XMLSymbols.fENUMERATIONSymbol) {
                    ok = false;
                    for (int i = 0; i < enumeration.length; i++) {
                        if (defaultValue.equals(enumeration[i])) {
                            ok = true;
                        }
                    }
                }
            }
            if (!ok) {
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_ATT_DEFAULT_INVALID", new Object[] { attributeName, value }, XMLErrorReporter.SEVERITY_ERROR);
            }
        }
    }
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.attributeDecl(elementName, attributeName, type, enumeration, defaultType, defaultValue, nonNormalizedDefaultValue, augs);
    if (fDTDHandler != null) {
        fDTDHandler.attributeDecl(elementName, attributeName, type, enumeration, defaultType, defaultValue, nonNormalizedDefaultValue, augs);
    }
}

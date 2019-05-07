private Object validate(Object[] attrValues, String attr, String ivalue, int dvIndex, XSDocumentInfo schemaDoc) throws InvalidDatatypeValueException {
    if (ivalue == null)
        return null;
    // To validate these types, we don't actually need to normalize the  
    // strings. We only need to remove the whitespace from both ends.  
    // In some special cases (list types), StringTokenizer can correctly  
    // process the un-normalized whitespace.          
    String value = XMLChar.trim(ivalue);
    Object retValue = null;
    Vector memberType;
    int choice;
    switch(dvIndex) {
        case DT_BOOLEAN:
            if (value.equals(SchemaSymbols.ATTVAL_FALSE) || value.equals(SchemaSymbols.ATTVAL_FALSE_0)) {
                retValue = Boolean.FALSE;
            } else if (value.equals(SchemaSymbols.ATTVAL_TRUE) || value.equals(SchemaSymbols.ATTVAL_TRUE_1)) {
                retValue = Boolean.TRUE;
            } else {
                throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { value, "boolean" });
            }
            break;
        case DT_NONNEGINT:
            try {
                if (value.length() > 0 && value.charAt(0) == '+')
                    value = value.substring(1);
                retValue = fXIntPool.getXInt(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { value, "nonNegativeInteger" });
            }
            if (((XInt) retValue).intValue() < 0)
                throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { value, "nonNegativeInteger" });
            break;
        case DT_POSINT:
            try {
                if (value.length() > 0 && value.charAt(0) == '+')
                    value = value.substring(1);
                retValue = fXIntPool.getXInt(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { value, "positiveInteger" });
            }
            if (((XInt) retValue).intValue() <= 0)
                throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { value, "positiveInteger" });
            break;
        case DT_BLOCK:
            // block = (#all | List of (extension | restriction | substitution))  
            choice = 0;
            if (value.equals(SchemaSymbols.ATTVAL_POUNDALL)) {
                choice = XSConstants.DERIVATION_SUBSTITUTION | XSConstants.DERIVATION_EXTENSION | XSConstants.DERIVATION_RESTRICTION | XSConstants.DERIVATION_LIST | XSConstants.DERIVATION_UNION;
            } else {
                StringTokenizer t = new StringTokenizer(value, " \n\t\r");
                while (t.hasMoreTokens()) {
                    String token = t.nextToken();
                    if (token.equals(SchemaSymbols.ATTVAL_EXTENSION)) {
                        choice |= XSConstants.DERIVATION_EXTENSION;
                    } else if (token.equals(SchemaSymbols.ATTVAL_RESTRICTION)) {
                        choice |= XSConstants.DERIVATION_RESTRICTION;
                    } else if (token.equals(SchemaSymbols.ATTVAL_SUBSTITUTION)) {
                        choice |= XSConstants.DERIVATION_SUBSTITUTION;
                    } else {
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "(#all | List of (extension | restriction | substitution))" });
                    }
                }
            }
            retValue = fXIntPool.getXInt(choice);
            break;
        case DT_BLOCK1:
        case DT_FINAL:
            // block = (#all | List of (extension | restriction))  
            // final = (#all | List of (extension | restriction))  
            choice = 0;
            if (value.equals(SchemaSymbols.ATTVAL_POUNDALL)) {
                //choice = SchemaSymbols.EXTENSION|SchemaSymbols.RESTRICTION;  
                // REVISIT: if #all, then make the result the combination of  
                //          everything: substitution/externsion/restriction/list/union.  
                //          would this be a problem?  
                // the reason doing so is that when final/blockFinal on <schema>  
                // is #all, it's not always the same as the conbination of those  
                // values allowed by final/blockFinal.  
                // for example, finalDefault="#all" is not always the same as  
                // finalDefault="extension restriction".  
                // if finalDefault="#all", final on any simple type would be  
                // "extension restriction list union".  
                choice = XSConstants.DERIVATION_SUBSTITUTION | XSConstants.DERIVATION_EXTENSION | XSConstants.DERIVATION_RESTRICTION | XSConstants.DERIVATION_LIST | XSConstants.DERIVATION_UNION;
            } else {
                StringTokenizer t = new StringTokenizer(value, " \n\t\r");
                while (t.hasMoreTokens()) {
                    String token = t.nextToken();
                    if (token.equals(SchemaSymbols.ATTVAL_EXTENSION)) {
                        choice |= XSConstants.DERIVATION_EXTENSION;
                    } else if (token.equals(SchemaSymbols.ATTVAL_RESTRICTION)) {
                        choice |= XSConstants.DERIVATION_RESTRICTION;
                    } else {
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "(#all | List of (extension | restriction))" });
                    }
                }
            }
            retValue = fXIntPool.getXInt(choice);
            break;
        case DT_FINAL1:
            // final = (#all | List of (list | union | restriction))  
            choice = 0;
            if (value.equals(SchemaSymbols.ATTVAL_POUNDALL)) {
                //choice = SchemaSymbols.RESTRICTION|SchemaSymbols.LIST|  
                //         SchemaSymbols.UNION;  
                // REVISIT: if #all, then make the result the combination of  
                //          everything: substitution/externsion/restriction/list/union.  
                //          would this be a problem?  
                // same reason as above DT_BLOCK1/DT_FINAL  
                choice = XSConstants.DERIVATION_SUBSTITUTION | XSConstants.DERIVATION_EXTENSION | XSConstants.DERIVATION_RESTRICTION | XSConstants.DERIVATION_LIST | XSConstants.DERIVATION_UNION;
            } else {
                StringTokenizer t = new StringTokenizer(value, " \n\t\r");
                while (t.hasMoreTokens()) {
                    String token = t.nextToken();
                    if (token.equals(SchemaSymbols.ATTVAL_LIST)) {
                        choice |= XSConstants.DERIVATION_LIST;
                    } else if (token.equals(SchemaSymbols.ATTVAL_UNION)) {
                        choice |= XSConstants.DERIVATION_UNION;
                    } else if (token.equals(SchemaSymbols.ATTVAL_RESTRICTION)) {
                        choice |= XSConstants.DERIVATION_RESTRICTION;
                    } else {
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "(#all | List of (list | union | restriction))" });
                    }
                }
            }
            retValue = fXIntPool.getXInt(choice);
            break;
        case DT_FINAL2:
            // finalDefault = (#all | List of (extension | restriction | list | union))  
            choice = 0;
            if (value.equals(SchemaSymbols.ATTVAL_POUNDALL)) {
                //choice = SchemaSymbols.RESTRICTION|SchemaSymbols.LIST|  
                //         SchemaSymbols.UNION;  
                // REVISIT: if #all, then make the result the combination of  
                //          everything: substitution/externsion/restriction/list/union.  
                //          would this be a problem?  
                // same reason as above DT_BLOCK1/DT_FINAL  
                choice = XSConstants.DERIVATION_SUBSTITUTION | XSConstants.DERIVATION_EXTENSION | XSConstants.DERIVATION_RESTRICTION | XSConstants.DERIVATION_LIST | XSConstants.DERIVATION_UNION;
            } else {
                StringTokenizer t = new StringTokenizer(value, " \n\t\r");
                while (t.hasMoreTokens()) {
                    String token = t.nextToken();
                    if (token.equals(SchemaSymbols.ATTVAL_EXTENSION)) {
                        choice |= XSConstants.DERIVATION_EXTENSION;
                    } else if (token.equals(SchemaSymbols.ATTVAL_RESTRICTION)) {
                        choice |= XSConstants.DERIVATION_RESTRICTION;
                    } else if (token.equals(SchemaSymbols.ATTVAL_LIST)) {
                        choice |= XSConstants.DERIVATION_LIST;
                    } else if (token.equals(SchemaSymbols.ATTVAL_UNION)) {
                        choice |= XSConstants.DERIVATION_UNION;
                    } else {
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "(#all | List of (extension | restriction | list | union))" });
                    }
                }
            }
            retValue = fXIntPool.getXInt(choice);
            break;
        case DT_FORM:
            // form = (qualified | unqualified)  
            if (value.equals(SchemaSymbols.ATTVAL_QUALIFIED))
                retValue = INT_QUALIFIED;
            else if (value.equals(SchemaSymbols.ATTVAL_UNQUALIFIED))
                retValue = INT_UNQUALIFIED;
            else
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(qualified | unqualified)" });
            break;
        case DT_MAXOCCURS:
            // maxOccurs = (nonNegativeInteger | unbounded)  
            if (value.equals(SchemaSymbols.ATTVAL_UNBOUNDED)) {
                retValue = INT_UNBOUNDED;
            } else {
                try {
                    retValue = validate(attrValues, attr, value, DT_NONNEGINT, schemaDoc);
                } catch (NumberFormatException e) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "(nonNegativeInteger | unbounded)" });
                }
            }
            break;
        case DT_MAXOCCURS1:
            // maxOccurs = 1  
            if (value.equals("1"))
                retValue = fXIntPool.getXInt(1);
            else
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(1)" });
            break;
        case DT_MEMBERTYPES:
            // memberTypes = List of QName  
            memberType = new Vector();
            try {
                StringTokenizer t = new StringTokenizer(value, " \n\t\r");
                while (t.hasMoreTokens()) {
                    String token = t.nextToken();
                    QName qname = (QName) fExtraDVs[DT_QNAME].validate(token, schemaDoc.fValidationContext, null);
                    // kludge to handle chameleon includes/redefines...  
                    if (qname.prefix == XMLSymbols.EMPTY_STRING && qname.uri == null && schemaDoc.fIsChameleonSchema)
                        qname.uri = schemaDoc.fTargetNamespace;
                    memberType.addElement(qname);
                }
                retValue = memberType;
            } catch (InvalidDatatypeValueException ide) {
                throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.2", new Object[] { value, "(List of QName)" });
            }
            break;
        case DT_MINOCCURS1:
            // minOccurs = (0 | 1)  
            if (value.equals("0"))
                retValue = fXIntPool.getXInt(0);
            else if (value.equals("1"))
                retValue = fXIntPool.getXInt(1);
            else
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(0 | 1)" });
            break;
        case DT_NAMESPACE:
            // namespace = ((##any | ##other) | List of (anyURI | (##targetNamespace | ##local)) )  
            if (value.equals(SchemaSymbols.ATTVAL_TWOPOUNDANY)) {
                // ##any  
                retValue = INT_ANY_ANY;
            } else if (value.equals(SchemaSymbols.ATTVAL_TWOPOUNDOTHER)) {
                // ##other  
                retValue = INT_ANY_NOT;
                String[] list = new String[2];
                list[0] = schemaDoc.fTargetNamespace;
                list[1] = null;
                attrValues[ATTIDX_NAMESPACE_LIST] = list;
            } else {
                // list  
                retValue = INT_ANY_LIST;
                fNamespaceList.removeAllElements();
                // tokenize  
                StringTokenizer tokens = new StringTokenizer(value, " \n\t\r");
                String token;
                String tempNamespace;
                try {
                    while (tokens.hasMoreTokens()) {
                        token = tokens.nextToken();
                        if (token.equals(SchemaSymbols.ATTVAL_TWOPOUNDLOCAL)) {
                            tempNamespace = null;
                        } else if (token.equals(SchemaSymbols.ATTVAL_TWOPOUNDTARGETNS)) {
                            tempNamespace = schemaDoc.fTargetNamespace;
                        } else {
                            // we have found namespace URI here  
                            // need to add it to the symbol table  
                            fExtraDVs[DT_ANYURI].validate(token, schemaDoc.fValidationContext, null);
                            tempNamespace = fSymbolTable.addSymbol(token);
                        }
                        //check for duplicate namespaces in the list  
                        if (!fNamespaceList.contains(tempNamespace)) {
                            fNamespaceList.addElement(tempNamespace);
                        }
                    }
                } catch (InvalidDatatypeValueException ide) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "((##any | ##other) | List of (anyURI | (##targetNamespace | ##local)) )" });
                }
                // convert the vector to an array  
                int num = fNamespaceList.size();
                String[] list = new String[num];
                fNamespaceList.copyInto(list);
                attrValues[ATTIDX_NAMESPACE_LIST] = list;
            }
            break;
        case DT_PROCESSCONTENTS:
            // processContents = (lax | skip | strict)  
            if (value.equals(SchemaSymbols.ATTVAL_STRICT))
                retValue = INT_ANY_STRICT;
            else if (value.equals(SchemaSymbols.ATTVAL_LAX))
                retValue = INT_ANY_LAX;
            else if (value.equals(SchemaSymbols.ATTVAL_SKIP))
                retValue = INT_ANY_SKIP;
            else
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(lax | skip | strict)" });
            break;
        case DT_USE:
            // use = (optional | prohibited | required)  
            if (value.equals(SchemaSymbols.ATTVAL_OPTIONAL))
                retValue = INT_USE_OPTIONAL;
            else if (value.equals(SchemaSymbols.ATTVAL_REQUIRED))
                retValue = INT_USE_REQUIRED;
            else if (value.equals(SchemaSymbols.ATTVAL_PROHIBITED))
                retValue = INT_USE_PROHIBITED;
            else
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(optional | prohibited | required)" });
            break;
        case DT_WHITESPACE:
            // value = preserve | replace | collapse  
            if (value.equals(SchemaSymbols.ATTVAL_PRESERVE))
                retValue = INT_WS_PRESERVE;
            else if (value.equals(SchemaSymbols.ATTVAL_REPLACE))
                retValue = INT_WS_REPLACE;
            else if (value.equals(SchemaSymbols.ATTVAL_COLLAPSE))
                retValue = INT_WS_COLLAPSE;
            else
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(preserve | replace | collapse)" });
            break;
    }
    return retValue;
}

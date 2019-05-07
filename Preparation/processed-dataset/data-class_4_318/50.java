XSTypeDefinition getAndCheckXsiType(QName element, String xsiType, XMLAttributes attributes) {
    // This method also deals with clause 1.2.1.2 of the constraint  
    // Validation Rule: Schema-Validity Assessment (Element)  
    // Element Locally Valid (Element)  
    // 4 If there is an attribute information item among the element information item's [attributes] whose [namespace name] is identical to http://www.w3.org/2001/XMLSchema-instance and whose [local name] is type, then all of the following must be true:  
    // 4.1 The normalized value of that attribute information item must be valid with respect to the built-in QName simple type, as defined by String Valid (3.14.4);  
    QName typeName = null;
    try {
        typeName = (QName) fQNameDV.validate(xsiType, fValidationState, null);
    } catch (InvalidDatatypeValueException e) {
        reportSchemaError(e.getKey(), e.getArgs());
        reportSchemaError("cvc-elt.4.1", new Object[] { element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_TYPE, xsiType });
        return null;
    }
    // 4.2 The local name and namespace name (as defined in QName Interpretation (3.15.3)), of the actual value of that attribute information item must resolve to a type definition, as defined in QName resolution (Instance) (3.15.4)  
    XSTypeDefinition type = null;
    // if the namespace is schema namespace, first try built-in types  
    if (typeName.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
        type = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(typeName.localpart);
    }
    // if it's not schema built-in types, then try to get a grammar  
    if (type == null) {
        //try to find schema grammar by different means....  
        SchemaGrammar grammar = findSchemaGrammar(XSDDescription.CONTEXT_XSITYPE, typeName.uri, element, typeName, attributes);
        if (grammar != null)
            type = grammar.getGlobalTypeDecl(typeName.localpart);
    }
    // still couldn't find the type, report an error  
    if (type == null) {
        reportSchemaError("cvc-elt.4.2", new Object[] { element.rawname, xsiType });
        return null;
    }
    // if there is no current type, set this one as current.  
    // and we don't need to do extra checking  
    if (fCurrentType != null) {
        short block = XSConstants.DERIVATION_NONE;
        // 4.3 The local type definition must be validly derived from the {type definition} given the union of the {disallowed substitutions} and the {type definition}'s {prohibited substitutions}, as defined in Type Derivation OK (Complex) (3.4.6) (if it is a complex type definition), or given {disallowed substitutions} as defined in Type Derivation OK (Simple) (3.14.6) (if it is a simple type definition).  
        // Note: It's possible to have fCurrentType be non-null and fCurrentElemDecl  
        // be null, if the current type is set using the property "root-type-definition".  
        // In that case, we don't disallow any substitutions. -PM  
        if (fCurrentElemDecl != null) {
            block = fCurrentElemDecl.fBlock;
        }
        if (fCurrentType.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
            block |= ((XSComplexTypeDecl) fCurrentType).fBlock;
        }
        if (!XSConstraints.checkTypeDerivationOk(type, fCurrentType, block)) {
            reportSchemaError("cvc-elt.4.3", new Object[] { element.rawname, xsiType, fCurrentType.getName() });
        }
    }
    return type;
}

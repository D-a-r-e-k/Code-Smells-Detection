protected Object getGlobalDeclFromGrammar(SchemaGrammar sGrammar, int declType, String localpart, String schemaLoc) {
    Object retObj = null;
    switch(declType) {
        case ATTRIBUTE_TYPE:
            retObj = sGrammar.getGlobalAttributeDecl(localpart, schemaLoc);
            break;
        case ATTRIBUTEGROUP_TYPE:
            retObj = sGrammar.getGlobalAttributeGroupDecl(localpart, schemaLoc);
            break;
        case ELEMENT_TYPE:
            retObj = sGrammar.getGlobalElementDecl(localpart, schemaLoc);
            break;
        case GROUP_TYPE:
            retObj = sGrammar.getGlobalGroupDecl(localpart, schemaLoc);
            break;
        case IDENTITYCONSTRAINT_TYPE:
            retObj = sGrammar.getIDConstraintDecl(localpart, schemaLoc);
            break;
        case NOTATION_TYPE:
            retObj = sGrammar.getGlobalNotationDecl(localpart, schemaLoc);
            break;
        case TYPEDECL_TYPE:
            retObj = sGrammar.getGlobalTypeDecl(localpart, schemaLoc);
            break;
    }
    return retObj;
}

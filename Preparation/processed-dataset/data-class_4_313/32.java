protected Object getGlobalDeclFromGrammar(SchemaGrammar sGrammar, int declType, String localpart) {
    Object retObj = null;
    switch(declType) {
        case ATTRIBUTE_TYPE:
            retObj = sGrammar.getGlobalAttributeDecl(localpart);
            break;
        case ATTRIBUTEGROUP_TYPE:
            retObj = sGrammar.getGlobalAttributeGroupDecl(localpart);
            break;
        case ELEMENT_TYPE:
            retObj = sGrammar.getGlobalElementDecl(localpart);
            break;
        case GROUP_TYPE:
            retObj = sGrammar.getGlobalGroupDecl(localpart);
            break;
        case IDENTITYCONSTRAINT_TYPE:
            retObj = sGrammar.getIDConstraintDecl(localpart);
            break;
        case NOTATION_TYPE:
            retObj = sGrammar.getGlobalNotationDecl(localpart);
            break;
        case TYPEDECL_TYPE:
            retObj = sGrammar.getGlobalTypeDecl(localpart);
            break;
    }
    return retObj;
}

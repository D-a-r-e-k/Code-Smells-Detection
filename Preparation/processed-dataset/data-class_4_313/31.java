// getGlobalDecl(XSDocumentInfo, int, QName):  Object  
// If we are tolerating duplicate declarations and allowing namespace growth  
// use the declaration from the current schema load (if it exists)  
protected Object getGlobalDecl(String declKey, int declType) {
    Object retObj = null;
    switch(declType) {
        case ATTRIBUTE_TYPE:
            retObj = getGlobalAttributeDecl(declKey);
            break;
        case ATTRIBUTEGROUP_TYPE:
            retObj = getGlobalAttributeGroupDecl(declKey);
            break;
        case ELEMENT_TYPE:
            retObj = getGlobalElementDecl(declKey);
            break;
        case GROUP_TYPE:
            retObj = getGlobalGroupDecl(declKey);
            break;
        case IDENTITYCONSTRAINT_TYPE:
            retObj = getIDConstraintDecl(declKey);
            break;
        case NOTATION_TYPE:
            retObj = getGlobalNotationDecl(declKey);
            break;
        case TYPEDECL_TYPE:
            retObj = getGlobalTypeDecl(declKey);
            break;
    }
    return retObj;
}

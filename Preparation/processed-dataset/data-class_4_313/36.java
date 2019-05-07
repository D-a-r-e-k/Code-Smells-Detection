// This method determines whether there is a group  
// (attributeGroup) which the given one has redefined by  
// restriction.  If so, it returns it; else it returns null.  
// @param type:  whether what's been redefined is an  
// attributeGroup or a group;  
// @param name:  the QName of the component doing the redefining.  
// @param currSchema:  schema doc in which the redefining component lives.  
// @return:  Object representing decl redefined if present, null  
// otherwise.  
Object getGrpOrAttrGrpRedefinedByRestriction(int type, QName name, XSDocumentInfo currSchema, Element elmNode) {
    String realName = name.uri != null ? name.uri + "," + name.localpart : "," + name.localpart;
    String nameToFind = null;
    switch(type) {
        case ATTRIBUTEGROUP_TYPE:
            nameToFind = (String) fRedefinedRestrictedAttributeGroupRegistry.get(realName);
            break;
        case GROUP_TYPE:
            nameToFind = (String) fRedefinedRestrictedGroupRegistry.get(realName);
            break;
        default:
            return null;
    }
    if (nameToFind == null)
        return null;
    int commaPos = nameToFind.indexOf(",");
    QName qNameToFind = new QName(XMLSymbols.EMPTY_STRING, nameToFind.substring(commaPos + 1), nameToFind.substring(commaPos), (commaPos == 0) ? null : nameToFind.substring(0, commaPos));
    Object retObj = getGlobalDecl(currSchema, type, qNameToFind, elmNode);
    if (retObj == null) {
        switch(type) {
            case ATTRIBUTEGROUP_TYPE:
                reportSchemaError("src-redefine.7.2.1", new Object[] { name.localpart }, elmNode);
                break;
            case GROUP_TYPE:
                reportSchemaError("src-redefine.6.2.1", new Object[] { name.localpart }, elmNode);
                break;
        }
        return null;
    }
    return retObj;
}

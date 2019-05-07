// add a global attribute group decl from a current schema load (only if no existing decl is found)  
void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl) {
    final String namespace = decl.getNamespace();
    final String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
    if (fGlobalAttrGrpDecls.get(declKey) == null) {
        fGlobalAttrGrpDecls.put(declKey, decl);
    }
}

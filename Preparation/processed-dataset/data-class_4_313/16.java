// add a global attribute decl from a current schema load (only if no existing decl is found)  
void addGlobalAttributeDecl(XSAttributeDecl decl) {
    final String namespace = decl.getNamespace();
    final String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
    if (fGlobalAttrDecls.get(declKey) == null) {
        fGlobalAttrDecls.put(declKey, decl);
    }
}

// add a global element decl from a current schema load (only if no existing decl is found)  
void addGlobalElementDecl(XSElementDecl decl) {
    final String namespace = decl.getNamespace();
    final String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
    if (fGlobalElemDecls.get(declKey) == null) {
        fGlobalElemDecls.put(declKey, decl);
    }
}

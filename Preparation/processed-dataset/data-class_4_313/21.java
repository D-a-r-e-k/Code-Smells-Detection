// add a global type decl from a current schema load (only if no existing decl is found)  
void addGlobalTypeDecl(XSTypeDefinition decl) {
    final String namespace = decl.getNamespace();
    final String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
    if (fGlobalTypeDecls.get(declKey) == null) {
        fGlobalTypeDecls.put(declKey, decl);
    }
}

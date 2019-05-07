// add a global group decl from a current schema load (only if no existing decl is found)  
void addGlobalGroupDecl(XSGroupDecl decl) {
    final String namespace = decl.getNamespace();
    final String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
    if (fGlobalGroupDecls.get(declKey) == null) {
        fGlobalGroupDecls.put(declKey, decl);
    }
}

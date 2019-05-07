// add a global notation decl from a current schema load (only if no existing decl is found)  
void addGlobalNotationDecl(XSNotationDecl decl) {
    final String namespace = decl.getNamespace();
    final String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
    if (fGlobalNotationDecls.get(declKey) == null) {
        fGlobalNotationDecls.put(declKey, decl);
    }
}

// add a identity constraint decl from a current schema load (only if no existing decl is found)  
void addIDConstraintDecl(IdentityConstraint decl) {
    final String namespace = decl.getNamespace();
    final String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getIdentityConstraintName() : namespace + "," + decl.getIdentityConstraintName();
    if (fGlobalIDConstraintDecls.get(declKey) == null) {
        fGlobalIDConstraintDecls.put(declKey, decl);
    }
}

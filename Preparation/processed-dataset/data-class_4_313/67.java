private void addGlobalTypeDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
    XSNamedMap components = srcGrammar.getComponents(XSConstants.TYPE_DEFINITION);
    int len = components.getLength();
    XSTypeDefinition srcDecl, dstDecl;
    // add global components  
    for (int i = 0; i < len; i++) {
        srcDecl = (XSTypeDefinition) components.item(i);
        dstDecl = dstGrammar.getGlobalTypeDecl(srcDecl.getName());
        if (dstDecl == null) {
            dstGrammar.addGlobalTypeDecl(srcDecl);
        } else if (dstDecl != srcDecl && !fTolerateDuplicates) {
            reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
        }
    }
    // add any extended (duplicate) global components  
    ObjectList componentsExt = srcGrammar.getComponentsExt(XSConstants.TYPE_DEFINITION);
    len = componentsExt.getLength();
    for (int i = 0; i < len; i += 2) {
        final String key = (String) componentsExt.item(i);
        final int index = key.indexOf(',');
        final String location = key.substring(0, index);
        final String name = key.substring(index + 1, key.length());
        srcDecl = (XSTypeDefinition) componentsExt.item(i + 1);
        dstDecl = dstGrammar.getGlobalTypeDecl(name, location);
        if (dstDecl == null) {
            dstGrammar.addGlobalTypeDecl(srcDecl, location);
        } else if (dstDecl != srcDecl) {
        }
    }
}

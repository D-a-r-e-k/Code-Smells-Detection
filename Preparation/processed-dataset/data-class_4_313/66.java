private void addGlobalGroupDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
    XSNamedMap components = srcGrammar.getComponents(XSConstants.MODEL_GROUP_DEFINITION);
    int len = components.getLength();
    XSGroupDecl srcDecl, dstDecl;
    // add global components  
    for (int i = 0; i < len; i++) {
        srcDecl = (XSGroupDecl) components.item(i);
        dstDecl = dstGrammar.getGlobalGroupDecl(srcDecl.getName());
        if (dstDecl == null) {
            dstGrammar.addGlobalGroupDecl(srcDecl);
        } else if (srcDecl != dstDecl && !fTolerateDuplicates) {
            reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
        }
    }
    // add any extended (duplicate) global components  
    ObjectList componentsExt = srcGrammar.getComponentsExt(XSConstants.MODEL_GROUP_DEFINITION);
    len = componentsExt.getLength();
    for (int i = 0; i < len; i += 2) {
        final String key = (String) componentsExt.item(i);
        final int index = key.indexOf(',');
        final String location = key.substring(0, index);
        final String name = key.substring(index + 1, key.length());
        srcDecl = (XSGroupDecl) componentsExt.item(i + 1);
        dstDecl = dstGrammar.getGlobalGroupDecl(name, location);
        if (dstDecl == null) {
            dstGrammar.addGlobalGroupDecl(srcDecl, location);
        } else if (dstDecl != srcDecl) {
        }
    }
}

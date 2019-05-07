private void addGlobalAttributeDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
    XSNamedMap components = srcGrammar.getComponents(XSConstants.ATTRIBUTE_DECLARATION);
    int len = components.getLength();
    XSAttributeDecl srcDecl, dstDecl;
    // add global components  
    for (int i = 0; i < len; i++) {
        srcDecl = (XSAttributeDecl) components.item(i);
        dstDecl = dstGrammar.getGlobalAttributeDecl(srcDecl.getName());
        if (dstDecl == null) {
            dstGrammar.addGlobalAttributeDecl(srcDecl);
        } else if (dstDecl != srcDecl && !fTolerateDuplicates) {
            reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
        }
    }
    // add any extended (duplicate) global components  
    ObjectList componentsExt = srcGrammar.getComponentsExt(XSConstants.ATTRIBUTE_DECLARATION);
    len = componentsExt.getLength();
    for (int i = 0; i < len; i += 2) {
        final String key = (String) componentsExt.item(i);
        final int index = key.indexOf(',');
        final String location = key.substring(0, index);
        final String name = key.substring(index + 1, key.length());
        srcDecl = (XSAttributeDecl) componentsExt.item(i + 1);
        dstDecl = dstGrammar.getGlobalAttributeDecl(name, location);
        if (dstDecl == null) {
            dstGrammar.addGlobalAttributeDecl(srcDecl, location);
        } else if (dstDecl != srcDecl) {
        }
    }
}

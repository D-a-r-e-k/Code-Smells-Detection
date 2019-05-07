private void addGlobalAttributeGroupDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
    XSNamedMap components = srcGrammar.getComponents(XSConstants.ATTRIBUTE_GROUP);
    int len = components.getLength();
    XSAttributeGroupDecl srcDecl, dstDecl;
    // add global components  
    for (int i = 0; i < len; i++) {
        srcDecl = (XSAttributeGroupDecl) components.item(i);
        dstDecl = dstGrammar.getGlobalAttributeGroupDecl(srcDecl.getName());
        if (dstDecl == null) {
            dstGrammar.addGlobalAttributeGroupDecl(srcDecl);
        } else if (dstDecl != srcDecl && !fTolerateDuplicates) {
            reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
        }
    }
    // add any extended (duplicate) global components  
    ObjectList componentsExt = srcGrammar.getComponentsExt(XSConstants.ATTRIBUTE_GROUP);
    len = componentsExt.getLength();
    for (int i = 0; i < len; i += 2) {
        final String key = (String) componentsExt.item(i);
        final int index = key.indexOf(',');
        final String location = key.substring(0, index);
        final String name = key.substring(index + 1, key.length());
        srcDecl = (XSAttributeGroupDecl) componentsExt.item(i + 1);
        dstDecl = dstGrammar.getGlobalAttributeGroupDecl(name, location);
        if (dstDecl == null) {
            dstGrammar.addGlobalAttributeGroupDecl(srcDecl, location);
        } else if (dstDecl != srcDecl) {
        }
    }
}

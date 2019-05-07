private void addGlobalElementDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
    XSNamedMap components = srcGrammar.getComponents(XSConstants.ELEMENT_DECLARATION);
    int len = components.getLength();
    XSElementDecl srcDecl, dstDecl;
    // add global components  
    for (int i = 0; i < len; i++) {
        srcDecl = (XSElementDecl) components.item(i);
        dstDecl = dstGrammar.getGlobalElementDecl(srcDecl.getName());
        if (dstDecl == null) {
            dstGrammar.addGlobalElementDecl(srcDecl);
        } else if (dstDecl != srcDecl) {
        }
    }
    // add any extended (duplicate) global components  
    ObjectList componentsExt = srcGrammar.getComponentsExt(XSConstants.ELEMENT_DECLARATION);
    len = componentsExt.getLength();
    for (int i = 0; i < len; i += 2) {
        final String key = (String) componentsExt.item(i);
        final int index = key.indexOf(',');
        final String location = key.substring(0, index);
        final String name = key.substring(index + 1, key.length());
        srcDecl = (XSElementDecl) componentsExt.item(i + 1);
        dstDecl = dstGrammar.getGlobalElementDecl(name, location);
        if (dstDecl == null) {
            dstGrammar.addGlobalElementDecl(srcDecl, location);
        } else if (dstDecl != srcDecl) {
        }
    }
}

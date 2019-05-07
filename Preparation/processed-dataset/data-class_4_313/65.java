private void addGlobalNotationDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
    XSNamedMap components = srcGrammar.getComponents(XSConstants.NOTATION_DECLARATION);
    int len = components.getLength();
    XSNotationDecl srcDecl, dstDecl;
    // add global components  
    for (int i = 0; i < len; i++) {
        srcDecl = (XSNotationDecl) components.item(i);
        dstDecl = dstGrammar.getGlobalNotationDecl(srcDecl.getName());
        if (dstDecl == null) {
            dstGrammar.addGlobalNotationDecl(srcDecl);
        } else if (dstDecl != srcDecl && !fTolerateDuplicates) {
            reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
        }
    }
    // add any extended (duplicate) global components  
    ObjectList componentsExt = srcGrammar.getComponentsExt(XSConstants.NOTATION_DECLARATION);
    len = componentsExt.getLength();
    for (int i = 0; i < len; i += 2) {
        final String key = (String) componentsExt.item(i);
        final int index = key.indexOf(',');
        final String location = key.substring(0, index);
        final String name = key.substring(index + 1, key.length());
        srcDecl = (XSNotationDecl) componentsExt.item(i + 1);
        dstDecl = dstGrammar.getGlobalNotationDecl(name, location);
        if (dstDecl == null) {
            dstGrammar.addGlobalNotationDecl(srcDecl, location);
        } else if (dstDecl != srcDecl) {
        }
    }
}

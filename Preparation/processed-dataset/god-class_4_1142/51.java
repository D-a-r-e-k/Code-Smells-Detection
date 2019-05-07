// getSchemaDocument(String, XSInputSource, boolean, short, Element): Element  
private Vector expandGrammars(SchemaGrammar[] grammars) {
    Vector currGrammars = new Vector();
    for (int i = 0; i < grammars.length; i++) {
        if (!currGrammars.contains(grammars[i])) {
            currGrammars.add(grammars[i]);
        }
    }
    // for all (recursively) imported grammars  
    SchemaGrammar sg1, sg2;
    Vector gs;
    for (int i = 0; i < currGrammars.size(); i++) {
        // get the grammar  
        sg1 = (SchemaGrammar) currGrammars.elementAt(i);
        // we need to add grammars imported by sg1 too  
        gs = sg1.getImportedGrammars();
        // for all grammars imported by sg2, but not in the vector  
        // we add them to the vector  
        if (gs == null) {
            continue;
        }
        for (int j = gs.size() - 1; j >= 0; j--) {
            sg2 = (SchemaGrammar) gs.elementAt(j);
            if (!currGrammars.contains(sg2)) {
                currGrammars.addElement(sg2);
            }
        }
    }
    return currGrammars;
}

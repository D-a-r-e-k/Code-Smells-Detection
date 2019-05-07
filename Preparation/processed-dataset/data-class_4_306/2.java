// reset(XMLComponentManager)  
protected void reset() {
    // clear grammars  
    fDTDGrammar = null;
    // initialize state  
    fInDTDIgnore = false;
    fNDataDeclNotations.clear();
    // datatype validators  
    if (fValidation) {
        if (fNotationEnumVals == null) {
            fNotationEnumVals = new HashMap();
        }
        fNotationEnumVals.clear();
        fTableOfIDAttributeNames = new HashMap();
        fTableOfNOTATIONAttributeNames = new HashMap();
    }
}

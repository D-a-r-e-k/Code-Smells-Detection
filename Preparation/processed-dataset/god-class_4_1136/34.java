// validateDTDattribute(QName,String,XMLAttributeDecl)  
/** Returns true if invalid standalone attribute definition. */
protected boolean invalidStandaloneAttDef(QName element, QName attribute) {
    // REVISIT: This obviously needs to be fixed! -Ac  
    boolean state = true;
    /*
       if (fStandaloneReader == -1) {
          return false;
       }
       // we are normalizing a default att value...  this ok?
       if (element.rawname == -1) {
          return false;
       }
       return getAttDefIsExternal(element, attribute);
       */
    return state;
}

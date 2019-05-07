//getAndCheckXsiType  
boolean getXsiNil(QName element, String xsiNil) {
    // Element Locally Valid (Element)  
    // 3 The appropriate case among the following must be true:  
    // 3.1 If {nillable} is false, then there must be no attribute information item among the element information item's [attributes] whose [namespace name] is identical to http://www.w3.org/2001/XMLSchema-instance and whose [local name] is nil.  
    if (fCurrentElemDecl != null && !fCurrentElemDecl.getNillable()) {
        reportSchemaError("cvc-elt.3.1", new Object[] { element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL });
    } else {
        String value = XMLChar.trim(xsiNil);
        if (value.equals(SchemaSymbols.ATTVAL_TRUE) || value.equals(SchemaSymbols.ATTVAL_TRUE_1)) {
            if (fCurrentElemDecl != null && fCurrentElemDecl.getConstraintType() == XSConstants.VC_FIXED) {
                reportSchemaError("cvc-elt.3.2.2", new Object[] { element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL });
            }
            return true;
        }
    }
    return false;
}

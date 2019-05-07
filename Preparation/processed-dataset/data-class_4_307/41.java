// getAttributeTypeName(XMLAttributeDecl):String  
/** initialization */
protected void init() {
    // datatype validators  
    if (fValidation || fDynamicValidation) {
        try {
            //REVISIT: datatypeRegistry + initialization of datatype   
            //         why do we cast to ListDatatypeValidator?  
            fValID = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDSymbol);
            fValIDRef = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSymbol);
            fValIDRefs = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSSymbol);
            fValENTITY = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITYSymbol);
            fValENTITIES = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITIESSymbol);
            fValNMTOKEN = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSymbol);
            fValNMTOKENS = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSSymbol);
            fValNOTATION = fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNOTATIONSymbol);
        } catch (Exception e) {
            // should never happen  
            e.printStackTrace(System.err);
        }
    }
}

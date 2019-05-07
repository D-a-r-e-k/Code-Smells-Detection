public final boolean validate() {
    // Do validation if all of the following are true:  
    // 1. The JAXP Schema Language property is not XML Schema  
    //    REVISIT: since only DTD and Schema are supported at this time,  
    //             such checking is sufficient. but if more schema types  
    //             are introduced in the future, we'll need to change it  
    //             to something like  
    //             (fSchemaType == null || fSchemaType == NS_XML_DTD)  
    // 2. One of the following is true (validation features)  
    // 2.1 Dynamic validation is off, and validation is on  
    // 2.2 Dynamic validation is on, and DOCTYPE was seen  
    // 3 Xerces schema validation feature is off, or DOCTYPE was seen.  
    return (fSchemaType != Constants.NS_XMLSCHEMA) && (!fDynamicValidation && fValidation || fDynamicValidation && fSeenDoctypeDecl) && (fDTDValidation || fSeenDoctypeDecl);
}

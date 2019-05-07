// validate attriubtes from non-schema namespaces  
// REVISIT: why we store the attributes in this way? why not just a list  
//          of structure {element node, attr name/qname, attr value)?  
// REVISIT: pass the proper element node to reportSchemaError  
public void checkNonSchemaAttributes(XSGrammarBucket grammarBucket) {
    // for all attributes  
    Iterator entries = fNonSchemaAttrs.entrySet().iterator();
    XSAttributeDecl attrDecl;
    while (entries.hasNext()) {
        Map.Entry entry = (Map.Entry) entries.next();
        // get name, uri, localpart  
        String attrRName = (String) entry.getKey();
        String attrURI = attrRName.substring(0, attrRName.indexOf(','));
        String attrLocal = attrRName.substring(attrRName.indexOf(',') + 1);
        // find associated grammar  
        SchemaGrammar sGrammar = grammarBucket.getGrammar(attrURI);
        if (sGrammar == null) {
            continue;
        }
        // and get the datatype validator, if there is one  
        attrDecl = sGrammar.getGlobalAttributeDecl(attrLocal);
        if (attrDecl == null) {
            continue;
        }
        XSSimpleType dv = (XSSimpleType) attrDecl.getTypeDefinition();
        if (dv == null) {
            continue;
        }
        // get all values appeared with this attribute name  
        Vector values = (Vector) entry.getValue();
        String elName;
        String attrName = (String) values.elementAt(0);
        // for each of the values  
        int count = values.size();
        for (int i = 1; i < count; i += 2) {
            elName = (String) values.elementAt(i);
            try {
                // and validate it using the XSSimpleType  
                // REVISIT: what would be the proper validation context?  
                //          guess we need to save that in the vectors too.  
                dv.validate((String) values.elementAt(i + 1), null, null);
            } catch (InvalidDatatypeValueException ide) {
                reportSchemaError("s4s-att-invalid-value", new Object[] { elName, attrName, ide.getMessage() }, null);
            }
        }
    }
}

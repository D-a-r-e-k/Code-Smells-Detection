// findQName(String, XSDocumentInfo):  String  
// This function looks among the children of curr for an element of type elementSought.  
// If it finds one, it evaluates whether its ref attribute contains a reference  
// to originalQName.  If it does, it returns 1 + the value returned by  
// calls to itself on all other children.  In all other cases it returns 0 plus  
// the sum of the values returned by calls to itself on curr's children.  
// It also resets the value of ref so that it will refer to the renamed type from the schema  
// being redefined.  
private int changeRedefineGroup(String originalQName, String elementSought, String newName, Element curr, XSDocumentInfo schemaDoc) {
    int result = 0;
    for (Element child = DOMUtil.getFirstChildElement(curr); child != null; child = DOMUtil.getNextSiblingElement(child)) {
        String name = DOMUtil.getLocalName(child);
        if (!name.equals(elementSought))
            result += changeRedefineGroup(originalQName, elementSought, newName, child, schemaDoc);
        else {
            String ref = child.getAttribute(SchemaSymbols.ATT_REF);
            if (ref.length() != 0) {
                String processedRef = findQName(ref, schemaDoc);
                if (originalQName.equals(processedRef)) {
                    String prefix = XMLSymbols.EMPTY_STRING;
                    int colonptr = ref.indexOf(":");
                    if (colonptr > 0) {
                        prefix = ref.substring(0, colonptr);
                        child.setAttribute(SchemaSymbols.ATT_REF, prefix + ":" + newName);
                    } else
                        child.setAttribute(SchemaSymbols.ATT_REF, newName);
                    result++;
                    if (elementSought.equals(SchemaSymbols.ELT_GROUP)) {
                        String minOccurs = child.getAttribute(SchemaSymbols.ATT_MINOCCURS);
                        String maxOccurs = child.getAttribute(SchemaSymbols.ATT_MAXOCCURS);
                        if (!((maxOccurs.length() == 0 || maxOccurs.equals("1")) && (minOccurs.length() == 0 || minOccurs.equals("1")))) {
                            reportSchemaError("src-redefine.6.1.2", new Object[] { ref }, child);
                        }
                    }
                }
            }
        }
    }
    return result;
}

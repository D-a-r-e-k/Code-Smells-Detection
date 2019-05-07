// return an array back to the pool  
public void returnAttrArray(Object[] attrArray, XSDocumentInfo schemaDoc) {
    // pop the namespace context  
    if (schemaDoc != null)
        schemaDoc.fNamespaceSupport.popContext();
    // if 1. the pool is full; 2. the array is null;  
    // 3. the array is of wrong size; 4. the array is already returned  
    // then we can't accept this array to be returned  
    if (fPoolPos == 0 || attrArray == null || attrArray.length != ATTIDX_COUNT || ((Boolean) attrArray[ATTIDX_ISRETURNED]).booleanValue()) {
        return;
    }
    // mark this array as returned  
    attrArray[ATTIDX_ISRETURNED] = Boolean.TRUE;
    // better clear nonschema vector  
    if (attrArray[ATTIDX_NONSCHEMA] != null)
        ((Vector) attrArray[ATTIDX_NONSCHEMA]).clear();
    // and put it into the pool  
    fArrayPool[--fPoolPos] = attrArray;
}

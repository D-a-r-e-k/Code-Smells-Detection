// end fillInLocalElemInfo(...)  
/** This method makes sure that
     * if this component is being redefined that it lives in the
     * right schema.  It then renames the component correctly.  If it
     * detects a collision--a duplicate definition--then it complains.
     * Note that redefines must be handled carefully:  if there
     * is a collision, it may be because we're redefining something we know about
     * or because we've found the thing we're redefining.
     */
void checkForDuplicateNames(String qName, int declType, Hashtable registry, Hashtable registry_sub, Element currComp, XSDocumentInfo currSchema) {
    Object objElem = null;
    // REVISIT:  when we add derivation checking, we'll have to make  
    // sure that ID constraint collisions don't necessarily result in error messages.  
    if ((objElem = registry.get(qName)) == null) {
        // need to check whether we have a global declaration in the corresponding  
        // grammar  
        if (fNamespaceGrowth && !fTolerateDuplicates) {
            checkForDuplicateNames(qName, declType, currComp);
        }
        // just add it in!  
        registry.put(qName, currComp);
        registry_sub.put(qName, currSchema);
    } else {
        Element collidingElem = (Element) objElem;
        XSDocumentInfo collidingElemSchema = (XSDocumentInfo) registry_sub.get(qName);
        if (collidingElem == currComp)
            return;
        Element elemParent = null;
        XSDocumentInfo redefinedSchema = null;
        // case where we've collided with a redefining element  
        // (the parent of the colliding element is a redefine)  
        boolean collidedWithRedefine = true;
        if ((DOMUtil.getLocalName((elemParent = DOMUtil.getParent(collidingElem))).equals(SchemaSymbols.ELT_REDEFINE))) {
            redefinedSchema = (XSDocumentInfo) (fRedefine2XSDMap.get(elemParent));
        } else if ((DOMUtil.getLocalName(DOMUtil.getParent(currComp)).equals(SchemaSymbols.ELT_REDEFINE))) {
            redefinedSchema = collidingElemSchema;
            collidedWithRedefine = false;
        }
        if (redefinedSchema != null) {
            //redefinition involved somehow  
            // If both components belong to the same document then  
            // report an error and return.  
            if (collidingElemSchema == currSchema) {
                reportSchemaError("sch-props-correct.2", new Object[] { qName }, currComp);
                return;
            }
            String newName = qName.substring(qName.lastIndexOf(',') + 1) + REDEF_IDENTIFIER;
            if (redefinedSchema == currSchema) {
                // object comp. okay here  
                // now have to do some renaming...  
                currComp.setAttribute(SchemaSymbols.ATT_NAME, newName);
                if (currSchema.fTargetNamespace == null) {
                    registry.put("," + newName, currComp);
                    registry_sub.put("," + newName, currSchema);
                } else {
                    registry.put(currSchema.fTargetNamespace + "," + newName, currComp);
                    registry_sub.put(currSchema.fTargetNamespace + "," + newName, currSchema);
                }
                // and take care of nested redefines by calling recursively:  
                if (currSchema.fTargetNamespace == null)
                    checkForDuplicateNames("," + newName, declType, registry, registry_sub, currComp, currSchema);
                else
                    checkForDuplicateNames(currSchema.fTargetNamespace + "," + newName, declType, registry, registry_sub, currComp, currSchema);
            } else {
                // we may be redefining the wrong schema  
                if (collidedWithRedefine) {
                    if (currSchema.fTargetNamespace == null)
                        checkForDuplicateNames("," + newName, declType, registry, registry_sub, currComp, currSchema);
                    else
                        checkForDuplicateNames(currSchema.fTargetNamespace + "," + newName, declType, registry, registry_sub, currComp, currSchema);
                } else {
                    // error that redefined element in wrong schema  
                    reportSchemaError("sch-props-correct.2", new Object[] { qName }, currComp);
                }
            }
        } else {
            // we've just got a flat-out collision (we tolerate duplicate  
            // declarations, only if they are defined in different schema  
            // documents)  
            if (!fTolerateDuplicates || fUnparsedRegistriesExt[declType].get(qName) == currSchema) {
                reportSchemaError("sch-props-correct.2", new Object[] { qName }, currComp);
            }
        }
    }
    // store the lastest current document info  
    if (fTolerateDuplicates) {
        fUnparsedRegistriesExt[declType].put(qName, currSchema);
    }
}

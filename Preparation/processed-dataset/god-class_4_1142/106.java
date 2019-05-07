// the purpose of this method is to take the component of the  
// specified type and rename references to itself so that they  
// refer to the object being redefined.  It takes special care of  
// <group>s and <attributeGroup>s to ensure that information  
// relating to implicit restrictions is preserved for those  
// traversers.  
private void renameRedefiningComponents(XSDocumentInfo currSchema, Element child, String componentType, String oldName, String newName) {
    if (componentType.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
        Element grandKid = DOMUtil.getFirstChildElement(child);
        if (grandKid == null) {
            reportSchemaError("src-redefine.5.a.a", null, child);
        } else {
            String grandKidName = DOMUtil.getLocalName(grandKid);
            if (grandKidName.equals(SchemaSymbols.ELT_ANNOTATION)) {
                grandKid = DOMUtil.getNextSiblingElement(grandKid);
            }
            if (grandKid == null) {
                reportSchemaError("src-redefine.5.a.a", null, child);
            } else {
                grandKidName = DOMUtil.getLocalName(grandKid);
                if (!grandKidName.equals(SchemaSymbols.ELT_RESTRICTION)) {
                    reportSchemaError("src-redefine.5.a.b", new Object[] { grandKidName }, child);
                } else {
                    Object[] attrs = fAttributeChecker.checkAttributes(grandKid, false, currSchema);
                    QName derivedBase = (QName) attrs[XSAttributeChecker.ATTIDX_BASE];
                    if (derivedBase == null || derivedBase.uri != currSchema.fTargetNamespace || !derivedBase.localpart.equals(oldName)) {
                        reportSchemaError("src-redefine.5.a.c", new Object[] { grandKidName, (currSchema.fTargetNamespace == null ? "" : currSchema.fTargetNamespace) + "," + oldName }, child);
                    } else {
                        // now we have to do the renaming...  
                        if (derivedBase.prefix != null && derivedBase.prefix.length() > 0)
                            grandKid.setAttribute(SchemaSymbols.ATT_BASE, derivedBase.prefix + ":" + newName);
                        else
                            grandKid.setAttribute(SchemaSymbols.ATT_BASE, newName);
                    }
                    fAttributeChecker.returnAttrArray(attrs, currSchema);
                }
            }
        }
    } else if (componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
        Element grandKid = DOMUtil.getFirstChildElement(child);
        if (grandKid == null) {
            reportSchemaError("src-redefine.5.b.a", null, child);
        } else {
            if (DOMUtil.getLocalName(grandKid).equals(SchemaSymbols.ELT_ANNOTATION)) {
                grandKid = DOMUtil.getNextSiblingElement(grandKid);
            }
            if (grandKid == null) {
                reportSchemaError("src-redefine.5.b.a", null, child);
            } else {
                // have to go one more level down; let another pass worry whether complexType is valid.  
                Element greatGrandKid = DOMUtil.getFirstChildElement(grandKid);
                if (greatGrandKid == null) {
                    reportSchemaError("src-redefine.5.b.b", null, grandKid);
                } else {
                    String greatGrandKidName = DOMUtil.getLocalName(greatGrandKid);
                    if (greatGrandKidName.equals(SchemaSymbols.ELT_ANNOTATION)) {
                        greatGrandKid = DOMUtil.getNextSiblingElement(greatGrandKid);
                    }
                    if (greatGrandKid == null) {
                        reportSchemaError("src-redefine.5.b.b", null, grandKid);
                    } else {
                        greatGrandKidName = DOMUtil.getLocalName(greatGrandKid);
                        if (!greatGrandKidName.equals(SchemaSymbols.ELT_RESTRICTION) && !greatGrandKidName.equals(SchemaSymbols.ELT_EXTENSION)) {
                            reportSchemaError("src-redefine.5.b.c", new Object[] { greatGrandKidName }, greatGrandKid);
                        } else {
                            Object[] attrs = fAttributeChecker.checkAttributes(greatGrandKid, false, currSchema);
                            QName derivedBase = (QName) attrs[XSAttributeChecker.ATTIDX_BASE];
                            if (derivedBase == null || derivedBase.uri != currSchema.fTargetNamespace || !derivedBase.localpart.equals(oldName)) {
                                reportSchemaError("src-redefine.5.b.d", new Object[] { greatGrandKidName, (currSchema.fTargetNamespace == null ? "" : currSchema.fTargetNamespace) + "," + oldName }, greatGrandKid);
                            } else {
                                // now we have to do the renaming...  
                                if (derivedBase.prefix != null && derivedBase.prefix.length() > 0)
                                    greatGrandKid.setAttribute(SchemaSymbols.ATT_BASE, derivedBase.prefix + ":" + newName);
                                else
                                    greatGrandKid.setAttribute(SchemaSymbols.ATT_BASE, newName);
                            }
                        }
                    }
                }
            }
        }
    } else if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
        String processedBaseName = (currSchema.fTargetNamespace == null) ? "," + oldName : currSchema.fTargetNamespace + "," + oldName;
        int attGroupRefsCount = changeRedefineGroup(processedBaseName, componentType, newName, child, currSchema);
        if (attGroupRefsCount > 1) {
            reportSchemaError("src-redefine.7.1", new Object[] { new Integer(attGroupRefsCount) }, child);
        } else if (attGroupRefsCount == 1) {
        } else if (currSchema.fTargetNamespace == null)
            fRedefinedRestrictedAttributeGroupRegistry.put(processedBaseName, "," + newName);
        else
            fRedefinedRestrictedAttributeGroupRegistry.put(processedBaseName, currSchema.fTargetNamespace + "," + newName);
    } else if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
        String processedBaseName = (currSchema.fTargetNamespace == null) ? "," + oldName : currSchema.fTargetNamespace + "," + oldName;
        int groupRefsCount = changeRedefineGroup(processedBaseName, componentType, newName, child, currSchema);
        if (groupRefsCount > 1) {
            reportSchemaError("src-redefine.6.1.1", new Object[] { new Integer(groupRefsCount) }, child);
        } else if (groupRefsCount == 1) {
        } else {
            if (currSchema.fTargetNamespace == null)
                fRedefinedRestrictedGroupRegistry.put(processedBaseName, "," + newName);
            else
                fRedefinedRestrictedGroupRegistry.put(processedBaseName, currSchema.fTargetNamespace + "," + newName);
        }
    } else {
        reportSchemaError("Internal-Error", new Object[] { "could not handle this particular <redefine>; please submit your schemas and instance document in a bug report!" }, child);
    }
}

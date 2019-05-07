/**
    * Determine the relations that are defined for an entity EJB.
    *
    * @param entityEJBs from the jag configuration file.
    * @param entityEJBMap Current classes in the model.
    * @param simpleModel The UML model.
    */
private void createContainerManagedRelations(ArrayList entityEJBs, HashMap entityEJBMap, SimpleModel simpleModel) {
    for (int i = 0; i < entityEJBs.size(); i++) {
        Entity e = (Entity) entityEJBs.get(i);
        List relationFieldNames = e.getRelations();
        for (int j = 0; j < relationFieldNames.size(); j++) {
            Relation rel = (Relation) relationFieldNames.get(j);
            String source = e.getRefName();
            String destination = rel.getRelatedEntity().getRefName();
            boolean navigable = rel.isBidirectional();
            int targetCardinality = -1;
            if (rel.isTargetMultiple()) {
                // -1 means many.  
                targetCardinality = -1;
            } else {
                targetCardinality = 1;
            }
            SimpleUmlClass sourceClass = (SimpleUmlClass) entityEJBMap.get(source);
            SimpleUmlClass destinationClass = (SimpleUmlClass) entityEJBMap.get(destination);
            if (sourceClass != null && destinationClass != null) {
                SimpleAssociationEnd sourceEnd = new SimpleAssociationEnd(sourceClass.getName(), sourceClass, 0, 1, navigable);
                SimpleAssociationEnd destinationEnd = new SimpleAssociationEnd(destinationClass.getName(), destinationClass, 0, targetCardinality, true);
                SimpleAssociation assoc = new SimpleAssociation(rel.getFieldName().toString(), sourceEnd, destinationEnd);
                simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_ASSOCIATION_FOREIGN_FIELD, rel.getForeignPkFieldName().toString(), assoc);
                simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_ASSOCIATION_MULTIPLICITY, rel.isTargetMultiple() ? JagUMLProfile.TAGGED_VALUE_ASSOCIATION_MULTIPLICITY_MANY_TO_ONE : JagUMLProfile.TAGGED_VALUE_ASSOCIATION_MULTIPLICITY_ONE_TO_ONE, assoc);
                simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_ASSOCIATION_BIDIRECTIONAL, rel.isBidirectional() ? "true" : "false", assoc);
                simpleModel.addSimpleAssociation(assoc);
            }
        }
    }
}

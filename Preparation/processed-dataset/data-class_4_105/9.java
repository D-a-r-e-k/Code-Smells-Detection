/**
    * Determine the relations that are defined for an entity EJB.
    *
    * @param entityEJBMap Current classes in the model.
    * @param simpleModel The UML model.
    */
private void createContainerManagedRelations(HashMap entityEJBMap, SimpleModel simpleModel) {
    Iterator assocs = simpleModel.getSimpleAssociations().iterator();
    while (assocs.hasNext()) {
        SimpleAssociation assoc = (SimpleAssociation) assocs.next();
        String sourceEntityName = assoc.getSource().getSimpleClassifier().getName();
        String destinationEntityName = assoc.getDestination().getSimpleClassifier().getName();
        Entity sourceEntity = (Entity) entityEJBMap.get(sourceEntityName);
        Entity destinationEntity = (Entity) entityEJBMap.get(destinationEntityName);
        boolean multiplicity;
        boolean bidirectional;
        if (JagUMLProfile.TAGGED_VALUE_ASSOCIATION_MULTIPLICITY_ONE_TO_ONE.equals(simpleModel.getTaggedValue(JagUMLProfile.TAGGED_VALUE_ASSOCIATION_MULTIPLICITY, assoc))) {
            multiplicity = false;
        } else {
            // This is the default.  
            multiplicity = true;
        }
        if ("true".equals(simpleModel.getTaggedValue(JagUMLProfile.TAGGED_VALUE_ASSOCIATION_BIDIRECTIONAL, assoc))) {
            bidirectional = true;
        } else {
            // This is the default.  
            bidirectional = false;
        }
        if (sourceEntity == null || destinationEntity == null) {
            log("The relation named '" + assoc.getName() + "' has 1 or more 'association ends' " + "whose names do not correspond to entity bean class names");
            continue;
        }
        ForeignKey info = new ForeignKey();
        String fkFieldName = assoc.getName();
        String fkColumnName = null;
        Iterator i = sourceEntity.getFields().iterator();
        while (i.hasNext()) {
            Field field = (Field) i.next();
            if (field.getName().equals(fkFieldName)) {
                fkColumnName = field.getColumnName();
            }
        }
        info.setPkTableName(destinationEntity.getLocalTableName().toString());
        info.setPkColumnName(destinationEntity.getPrimaryKey().getColumnName());
        info.setFkColumnName(fkColumnName);
        info.setFkName(fkFieldName);
        Relation relation = new Relation(sourceEntity, info, false);
        relation.setBidirectional(bidirectional);
        relation.setTargetMultiple(multiplicity);
        sourceEntity.addRelation(relation);
        log("Added relation: " + relation);
    }
}

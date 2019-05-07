/**
     * Updates objects concerned with 'local' side CMR relations, namely:
     * 1: for every foreign key Field object within an entity that takes part in a relation, set its Relation object.
     * 2: for every Relation object, set the Relation's local-side foreign key Field object.
     */
private void updateLocalSideRelations() {
    Iterator entities = root.getEntityEjbs().iterator();
    while (entities.hasNext()) {
        Entity entity = (Entity) entities.next();
        //for every entity..  
        for (int i = 0; i < entity.getChildCount(); i++) {
            TreeNode child = entity.getChildAt(i);
            //for every relation in that entity..  
            if (child instanceof Relation) {
                Relation relation = (Relation) child;
                String fkFieldName = relation.getFieldName().toString();
                for (int j = 0; j < entity.getChildCount(); j++) {
                    TreeNode child2 = entity.getChildAt(j);
                    //iterate through all this entity's Fields..  
                    if (child2 instanceof Field) {
                        Field field = (Field) child2;
                        //until we find the Field that matches the relation's fkFieldName  
                        if (field.getName().equals(fkFieldName)) {
                            field.setRelation(relation);
                            field.setForeignKey(true);
                            relation.setFieldName(field.getName().toString());
                            relation.setFkField(field);
                            if (relation.getLocalColumn() == null) {
                                relation.setLocalColumn(relation.getFkField().getColumnName());
                            }
                            logToConsole("relation " + relation + ": local-side fk field is " + entity.getName() + ":" + field);
                        }
                    }
                }
            }
        }
    }
}

/**
     * Updates objects concerned with the 'foreign' side of CMR relations, namely:
     * 1: for every Relation, set the foreign-side primary key Field object (the primary key of the entity on the
     * foreign side of the relation).
     */
private void updateForeignSideRelations() {
    Iterator entities = root.getEntityEjbs().iterator();
    while (entities.hasNext()) {
        Entity entity = (Entity) entities.next();
        for (int i = 0; i < entity.getChildCount(); i++) {
            TreeNode child = entity.getChildAt(i);
            if (child instanceof Relation) {
                Relation relation = (Relation) child;
                Entity relatedEntity = relation.getRelatedEntity();
                String column = relation.getForeignColumn();
                for (int j = 0; j < relatedEntity.getChildCount(); j++) {
                    TreeNode child2 = relatedEntity.getChildAt(j);
                    if (child2 instanceof Field) {
                        Field field = (Field) child2;
                        if (field.getColumnName().equals(column)) {
                            relation.setForeignPkField(field);
                            logToConsole("relation " + relation + ": foreign-side pk is " + relatedEntity + ":" + field);
                        }
                    }
                }
            }
        }
    }
}

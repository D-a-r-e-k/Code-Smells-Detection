//GEN-LAST:event_addEntityMenuItemActionPerformed  
/**
     * Check all entities and determine if there is a possible many-to-many relation
     * This is the case if an entity has EXACLTY 2 many-to-one relations.
     * The entity will be marked as "Association entity" and the related entities,
     * that were marked as one-to-many, will be marked as many-to-many.
     */
private void checkForAssociationEntities(ArrayList createdEntities) {
    for (Iterator iterator = createdEntities.iterator(); iterator.hasNext(); ) {
        Entity entity = (Entity) iterator.next();
        if (entity.getRelations() != null && entity.getRelations().size() == 2 && entity.getFields().size() == 2) {
            // It's an entity with exaclty 2 foreign keys (targetMultiple is false).  
            if (((Relation) entity.getRelations().get(0)).isTargetMultiple() && ((Relation) entity.getRelations().get(1)).isTargetMultiple()) {
                // Mark the entity as an assocation entity.  
                entity.setIsAssociationEntity("true");
            }
        }
    }
}

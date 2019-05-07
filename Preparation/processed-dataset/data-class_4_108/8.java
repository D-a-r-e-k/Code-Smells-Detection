/**
     * Enables the presentation layer to specify that a given field within a given entity is a foreign key field.
     *
     * @param tableName the name of the table whose entity contains the field we're interested in.
     * @param fieldName the foreign key field.
     */
public static void setForeignKeyInField(String tableName, String fieldName) {
    TreeModel model = jagGenerator.tree.getModel();
    for (int i = 0; i < model.getChildCount(model.getRoot()); i++) {
        Object kid = model.getChild(model.getRoot(), i);
        if (kid instanceof Entity) {
            Entity entity = (Entity) kid;
            if (entity.getLocalTableName().equals(tableName)) {
                for (int j = 0; j < entity.getChildCount(); j++) {
                    Object kid2 = entity.getChildAt(j);
                    if (kid2 instanceof Field) {
                        Field field = (Field) kid2;
                        if (field.getName().toString().equals(fieldName)) {
                            field.setForeignKey(true);
                        }
                    }
                }
            }
        }
    }
}

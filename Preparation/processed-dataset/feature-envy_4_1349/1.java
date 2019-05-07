/**
    * Create all entity EJBs in the uml model and put them in a hashmap
    *
    * @param model the uml model.
    * @return HashMap with all Entity classes.
    */
private HashMap createEntityEJBs(SimpleModel model) {
    HashMap map = new HashMap();
    // Get a list of all packages in the model.  
    Collection pkList = model.getAllSimpleUmlPackages(model);
    for (Iterator pkIterator = pkList.iterator(); pkIterator.hasNext(); ) {
        SimpleUmlPackage simpleUmlPackage = (SimpleUmlPackage) pkIterator.next();
        Collection list;
        list = simpleUmlPackage.getSimpleClassifiers();
        for (Iterator pkit = list.iterator(); pkit.hasNext(); ) {
            SimpleModelElement el = (SimpleModelElement) pkit.next();
            if ((el instanceof SimpleUmlClass) && model.getStereoType(el) != null && model.getStereoType(el).equalsIgnoreCase(JagUMLProfile.STEREOTYPE_CLASS_ENTITY)) {
                // We got a winner, it's a class with the right stereotype.  
                SimpleUmlClass suc = (SimpleUmlClass) el;
                String rootPackage = simpleUmlPackage.getFullPackageName();
                String tableName = getTaggedValue(model, JagUMLProfile.TAGGED_VALUE_CLASS_TABLE_NAME, suc, Utils.unformat(Utils.firstToLowerCase(suc.getName())));
                Entity entity = new Entity(EMPTY_STRING, tableName, EMPTY_STRING);
                entity.setRootPackage(rootPackage);
                entity.setName(suc.getName());
                entity.setDescription(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, suc));
                if (model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DISPLAY_NAME, suc) != null) {
                    entity.setDisplayName(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DISPLAY_NAME, suc));
                }
                if (model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_IS_ASSOCIATION, suc) != null) {
                    entity.isAssociationEntity.setSelectedItem(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_IS_ASSOCIATION, suc));
                }
                entity.setRefName(entity.getName().toString());
                // Now iterate over the fields of the model element and Create fields that will be added to the entity.  
                int pkCount = 0;
                Collection attributes = suc.getSimpleAttributes();
                Field primaryKeyField = null;
                for (Iterator iterator = attributes.iterator(); iterator.hasNext(); ) {
                    SimpleAttribute att = (SimpleAttribute) iterator.next();
                    boolean isPK = equal(model.getStereoType(att), JagUMLProfile.STEREOTYPE_ATTRIBUTE_PRIMARY_KEY);
                    boolean required = false;
                    if (isPK) {
                        required = true;
                    } else {
                        // Only set required to true if a stereotype has been defined.  
                        required = equal(model.getStereoType(att), JagUMLProfile.STEREOTYPE_ATTRIBUTE_REQUIRED);
                    }
                    Column col = new Column();
                    String colName = model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_COLUMN_NAME, att);
                    if (colName == null) {
                        //make a column name based on the attribute name  
                        colName = Utils.unformat(att.getName());
                    }
                    col.setName(colName);
                    String sqlType = getTaggedValue(model, JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_SQL_TYPE, att, null);
                    String jdbcType = getTaggedValue(model, JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_JDBC_TYPE, att, null);
                    col.setPrimaryKey(isPK);
                    col.setNullable(!required);
                    SimpleClassifier theClassifier = att.getType();
                    String fieldType = theClassifier.getOwner().getFullPackageName();
                    if (fieldType != null && !Character.isLowerCase(theClassifier.getName().charAt(0))) {
                        fieldType = fieldType + DOT + theClassifier.getName();
                    } else {
                        //JAG doesn't support primitive entity bean persistant field types..  
                        String primitiveType = theClassifier.getName();
                        if (CHARACTER_PRIMITIVE.equals(primitiveType)) {
                            fieldType = CHARACTER_CLASS;
                        } else if (INTEGER_PRIMITIVE.equals(primitiveType)) {
                            fieldType = INTEGER_CLASS;
                        } else {
                            fieldType = JAVA_LANG_PACKAGE_PREFIX + Character.toUpperCase(primitiveType.charAt(0)) + primitiveType.substring(1);
                        }
                    }
                    if (sqlType == null) {
                        String[] mappedTypes = getDatabaseColumnTypesForClass(fieldType);
                        sqlType = mappedTypes[0];
                        jdbcType = mappedTypes[1];
                    }
                    col.setSqlType(sqlType);
                    String autoGeneratedPrimaryKey = model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_AUTO_PRIMARY_KEY, att);
                    boolean generate = false;
                    if ("true".equalsIgnoreCase(autoGeneratedPrimaryKey)) {
                        generate = true;
                    }
                    Field field = new Field(entity, col);
                    field.setName(att.getName());
                    field.setType(fieldType);
                    // Overrule the automatic setting by the Field constructor.  
                    if (isPK) {
                        field.setPrimaryKey(isPK);
                    }
                    field.setSqlType(sqlType);
                    field.setJdbcType(jdbcType);
                    field.setHasAutoGenPrimaryKey(generate);
                    if (isPK) {
                        pkCount++;
                        primaryKeyField = field;
                    }
                    entity.add(field);
                }
                if (pkCount > 1) {
                    // It's a composite primary key.  
                    entity.setIsComposite("true");
                    // Set the primary key class....  
                    String compositePK = model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_COMPOSITE_PRIMARY_KEY, suc);
                    entity.setPKeyType(compositePK);
                } else {
                    if (primaryKeyField != null) {
                        entity.setPrimaryKey(primaryKeyField);
                    }
                }
                if (pkCount == 0) {
                    log("UML Error! Entity '" + entity.getName() + "' has no primary key! At least one attribute " + "in an entity bean must have the stereotype \"PrimaryKey\".");
                    JOptionPane.showMessageDialog(null, "Entity '" + entity.getName() + "' has no primary key! At least one attribute " + "in an entity bean must have the stereotype \"PrimaryKey\".", "UML Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Put the entity in the hashmap entity  
                    map.put(entity.getRefName(), entity);
                }
            }
        }
    }
    return map;
}

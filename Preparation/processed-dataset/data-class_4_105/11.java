/** Create a class with the datasource definition */
private void createDataSource(SimpleModel model, Datasource ds) {
    boolean datasourceFound = false;
    // Get a list of all packages in the model.  
    Collection pkList = model.getAllSimpleUmlPackages(model);
    for (Iterator pkIterator = pkList.iterator(); pkIterator.hasNext(); ) {
        SimpleUmlPackage simpleUmlPackage = (SimpleUmlPackage) pkIterator.next();
        Collection list = simpleUmlPackage.getSimpleClassifiers();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            SimpleModelElement el = (SimpleModelElement) it.next();
            if ((el instanceof SimpleUmlClass) && model.getStereoType(el) != null && model.getStereoType(el).equals(JagUMLProfile.STEREOTYPE_CLASS_DATA_SOURCE)) {
                // We got a winner, it's a class with the right stereotype.  
                datasourceFound = true;
                ds.setJndi(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_JNDI_NAME, el));
                ds.setMapping(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_MAPPING, el));
                ds.setJdbcUrl(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_JDBC_URL, el));
                ds.setUserName(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_USER_NAME, el));
                ds.setPassword(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_PASSWORD, el));
            }
        }
    }
    if (!datasourceFound) {
        ds.setJndi("jdbc/" + model.getName());
    }
}

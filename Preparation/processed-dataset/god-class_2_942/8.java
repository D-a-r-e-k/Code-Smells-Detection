/** Create a class with the datasource definition */
private void createDataSource(Datasource ds, SimpleModel simpleModel) {
    SimpleUmlClass dsClass = new SimpleUmlClass("DataSource" + simpleModel.getName(), SimpleModelElement.PUBLIC);
    simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_CLASS_DATA_SOURCE, dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_JDBC_URL, ds.getJdbcUrl().toString(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_MAPPING, ds.getDatabase().getTypeMapping(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_USER_NAME, ds.getUserName().toString(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_PASSWORD, ds.getPassword().toString(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_JNDI_NAME, ds.getJndiName().toString(), dsClass);
    simpleModel.addSimpleClassifier(dsClass);
}

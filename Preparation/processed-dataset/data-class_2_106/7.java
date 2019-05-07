/** Create a class with all configuration settings for JAG. */
private void createConfigClass(Config config, App app, Paths paths, SimpleModel simpleModel) {
    String rootPackage = app.rootPackageText.getText();
    String projectName = app.descriptionText.getText();
    String logging = app.getLogFramework();
    SimpleUmlClass dsClass = new SimpleUmlClass("Config" + projectName, SimpleModelElement.PUBLIC);
    simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_CLASS_JAG_CONFIG, dsClass);
    // Config part.  
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_AUTHOR, config.getAuthorText(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_VERSION, config.getVersionText(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_COMPANY, config.getCompanyText(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TEMPLATE, config.getTemplate().getTemplateDir().toString(), dsClass);
    if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_APPLICATION_SERVER) != null) {
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_APPSERVER, (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_APPLICATION_SERVER), dsClass);
    }
    if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_RELATIONS) != null) {
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_RELATIONS, (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_RELATIONS), dsClass);
    }
    if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_MOCK) != null) {
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_MOCK, (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_MOCK), dsClass);
    }
    if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_JAVA5) != null) {
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_JAVA5, (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_JAVA5), dsClass);
    }
    if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_WEB_TIER) != null) {
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_WEB_TIER, (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_WEB_TIER), dsClass);
    }
    if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_BUSINESS_TIER) != null) {
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_BUSINESS_TIER, (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_BUSINESS_TIER), dsClass);
    }
    if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_SERVICE_TIER) != null) {
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_SERVICE_TIER, (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_SERVICE_TIER), dsClass);
    }
    // app part.  
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_APPLICATION_NAME, app.getName().toString(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_DESCRIPTION, projectName, dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_APPLICATION_VERSION, app.getVersion().toString(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_ROOT_PACKAGE, rootPackage, dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_LOGGING, logging, dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_DATE_FORMAT, app.getDateFormat().toString(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TIMESTAMP_FORMAT, app.getTimestampFormat().toString(), dsClass);
    // path part.  
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_SERVICE_PATH, paths.getServiceOutput(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_EJB_PATH, paths.getEjbOutput(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_WEB_PATH, paths.getWebOutput(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_JSP_PATH, paths.getJspOutput(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TEST_PATH, paths.getTestOutput(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_CONFIG_PATH, paths.getConfigOutput(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_SWING_PATH, paths.getSwingOutput(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_MOCK_PATH, paths.getMockOutput(), dsClass);
    simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_HIBERNATE_PATH, paths.getHibernateOutput(), dsClass);
    simpleModel.addSimpleClassifier(dsClass);
}

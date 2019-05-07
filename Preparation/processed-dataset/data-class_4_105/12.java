/** Create a class with the datasource definition */
private void createConfig(SimpleModel model, Config config, App app, Paths paths) {
    // Get a list of all packages in the model.  
    Collection pkList = model.getAllSimpleUmlPackages(model);
    for (Iterator pkIterator = pkList.iterator(); pkIterator.hasNext(); ) {
        SimpleUmlPackage simpleUmlPackage = (SimpleUmlPackage) pkIterator.next();
        Collection list = simpleUmlPackage.getSimpleClassifiers();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            SimpleModelElement el = (SimpleModelElement) it.next();
            if ((el instanceof SimpleUmlClass) && model.getStereoType(el) != null && model.getStereoType(el).equals(JagUMLProfile.STEREOTYPE_CLASS_JAG_CONFIG)) {
                // We got a winner, it's a class with the right stereotype.  
                config.setAuthor(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_AUTHOR, el));
                config.setVersion(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_VERSION, el));
                config.setCompany(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_COMPANY, el));
                String templateDir = model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TEMPLATE, el);
                if (templateDir != null) {
                    File dir = new File(templateDir);
                    config.getTemplate().setTemplateDir(dir);
                }
                HashMap map = new HashMap();
                map.put(JagGenerator.TEMPLATE_APPLICATION_SERVER, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_APPSERVER, el));
                map.put(JagGenerator.TEMPLATE_USE_RELATIONS, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_RELATIONS, el));
                map.put(JagGenerator.TEMPLATE_USE_JAVA5, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_JAVA5, el));
                map.put(JagGenerator.TEMPLATE_USE_MOCK, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_MOCK, el));
                map.put(JagGenerator.TEMPLATE_WEB_TIER, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_WEB_TIER, el));
                map.put(JagGenerator.TEMPLATE_BUSINESS_TIER, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_BUSINESS_TIER, el));
                map.put(JagGenerator.TEMPLATE_SERVICE_TIER, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_SERVICE_TIER, el));
                config.setTemplateSettings(map);
                app.setName(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_APPLICATION_NAME, el));
                app.setDescription(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_DESCRIPTION, el));
                app.setVersion(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_APPLICATION_VERSION, el));
                app.setRootPackage(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_ROOT_PACKAGE, el));
                app.setLogFramework(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_LOGGING, el));
                app.setDateFormat(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_DATE_FORMAT, el));
                app.setTimestampFormat(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TIMESTAMP_FORMAT, el));
                paths.setServiceOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_SERVICE_PATH, el));
                paths.setEjbOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_EJB_PATH, el));
                paths.setWebOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_WEB_PATH, el));
                paths.setJspOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_JSP_PATH, el));
                paths.setTestOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TEST_PATH, el));
                paths.setConfigOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_CONFIG_PATH, el));
                paths.setSwingOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_SWING_PATH, el));
                paths.setMockOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_MOCK_PATH, el));
                paths.setHibernateOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_HIBERNATE_PATH, el));
            }
        }
    }
}

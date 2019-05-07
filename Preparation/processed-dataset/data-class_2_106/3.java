private void generateXMI(Root root, File output) {
    App app = root.app;
    Config conf = root.config;
    // app.setRootPackage();  
    ArrayList sessionEJBs = root.getSessionEjbs();
    String rootPackage = app.rootPackageText.getText();
    String projectName = app.descriptionText.getText();
    SimpleModel simpleModel = new SimpleModel();
    simpleModel.setName(projectName);
    //SimpleDiagram diagram = new SimpleDiagram(projectName);  
    //simpleModel.addSimpleDiagram(diagram);  
    // First create the type packages.  
    createBasicTypes(simpleModel);
    simpleModel.addSimpleUmlPackage(rootPackage);
    createDataSource(root.datasource, simpleModel);
    createConfigClass(root.config, root.app, root.paths, simpleModel);
    // Create the entity EJBs.  
    HashMap entityEJBMap = createEntityEJBs(root.getEntityEjbs(), simpleModel);
    // Create the Session EJBs  
    HashMap sessionEJBMap = createSessionEJBs(root.getSessionEjbs(), simpleModel);
    // Create the dependencies for the session EJBs to the Entity EJBS.  
    // Session EJBs  
    for (int i = 0; i < sessionEJBs.size(); i++) {
        Session s = (Session) sessionEJBs.get(i);
        String refName = s.getRefName();
        // Session UML class is: sessionEJBMap  
        SimpleUmlClass sessionUMLClass = (SimpleUmlClass) sessionEJBMap.get(refName);
        ArrayList entityRefs = s.getEntityRefs();
        for (int j = 0; j < entityRefs.size(); j++) {
            String entityRefName = (String) entityRefs.get(j);
            SimpleUmlClass entityUMLClass = (SimpleUmlClass) entityEJBMap.get(entityRefName);
            if (entityUMLClass != null) {
                // Only add a reference if the entity could be found.  
                SimpleDependency dep = new SimpleDependency();
                dep.setClient(sessionUMLClass);
                dep.setSupplier(entityUMLClass);
                // Stereotype not required.  
                // simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_DEPENDENCTY_ENTITY_REF, dep);  
                simpleModel.addSimpleDependency(dep);
            }
        }
    }
    createContainerManagedRelations(root.getEntityEjbs(), entityEJBMap, simpleModel);
    try {
        if (output.isDirectory()) {
            output = new File(output, simpleModel.getName() + ".xmi");
        }
        OutputStream outputStream = new FileOutputStream(output);
        simpleModel.writeModel(outputStream);
    } catch (IOException ioe) {
        log("Error writing the file.");
    }
}

/**
    * Generate a config file readable by JAG from the UML model
    *
    * @param model UML Model
    * @return Root conifguration
    */
private Root generateConfig(SimpleModel model) throws InterruptedException {
    Root root = new Root();
    createDataSource(model, root.datasource);
    createConfig(model, root.config, root.app, root.paths);
    checkInterruptStatus();
    // Create the Session EJBs  
    HashMap sessionEJBMap = createSessionEJBs(model);
    if (sessionEJBMap != null && sessionEJBMap.size() > 0) {
        root.setSessionEjbs(new ArrayList(sessionEJBMap.values()));
    }
    checkInterruptStatus();
    // Create the entity EJBs.  
    HashMap entityEJBMap = createEntityEJBs(model);
    if (entityEJBMap != null && entityEJBMap.size() > 0) {
        root.setEntityEjbs(new ArrayList(entityEJBMap.values()));
    }
    checkInterruptStatus();
    // Create the container-managed relations  
    createContainerManagedRelations(entityEJBMap, model);
    return root;
}

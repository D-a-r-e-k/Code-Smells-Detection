/** Create the session EJBs on the simpleModel. */
private HashMap createSessionEJBs(SimpleModel model) {
    HashMap map = new HashMap();
    // Get a list of all packages in the model.  
    Collection pkList = model.getAllSimpleUmlPackages(model);
    for (Iterator pkIterator = pkList.iterator(); pkIterator.hasNext(); ) {
        SimpleUmlPackage simpleUmlPackage = (SimpleUmlPackage) pkIterator.next();
        Collection list = simpleUmlPackage.getSimpleClassifiers();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            SimpleModelElement el = (SimpleModelElement) it.next();
            if ((el instanceof SimpleUmlClass) && model.getStereoType(el) != null && model.getStereoType(el).equals(JagUMLProfile.STEREOTYPE_CLASS_SERVICE)) {
                // We got a winner, it's a class with the right stereotype.  
                ArrayList businessMethods = new ArrayList();
                SimpleUmlClass suc = (SimpleUmlClass) el;
                Collection operations = suc.getSimpleOperations();
                for (Iterator oit = operations.iterator(); oit.hasNext(); ) {
                    BusinessMethod bm = new BusinessMethod();
                    SimpleOperation operation = (SimpleOperation) oit.next();
                    log.info("The operation name is: " + operation.getName());
                    bm.setMethodName(operation.getName());
                    String desc = model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, operation);
                    if (desc == null) {
                        bm.setDescription("");
                    } else {
                        bm.setDescription(desc);
                    }
                    ArrayList argList = new ArrayList();
                    log.info("The number of parameters: " + operation.getSimpleParameters().size());
                    for (Iterator pit = operation.getSimpleParameters().iterator(); pit.hasNext(); ) {
                        SimpleParameter param = (SimpleParameter) pit.next();
                        BusinessArgument arg = new BusinessArgument();
                        String type;
                        // inout of return.  
                        log.debug("Param kind: " + param.getKind());
                        if (param.getType() != null) {
                            if (param.getType().getName() != null && param.getType().getName().equalsIgnoreCase("void")) {
                                type = "void";
                            } else {
                                String packageName = param.getType().getOwner().getFullPackageName();
                                if (packageName == null) {
                                    packageName = "";
                                } else {
                                    packageName = param.getType().getOwner().getFullPackageName() + ".";
                                }
                                String typeName = param.getType().getName();
                                if (typeName != null) {
                                    if (typeName.startsWith("java::lang::")) {
                                        // This is prepended by poseidon 3.  
                                        typeName = typeName.substring(12);
                                    }
                                }
                                type = "" + packageName + typeName;
                            }
                        } else {
                            type = "void";
                        }
                        if (param.getKind().equalsIgnoreCase(SimpleParameter.RETURN)) {
                            log.info("Found a return type");
                            bm.setReturnType(type);
                        } else {
                            log.info("The param name is: " + param.getName());
                            arg.setName(param.getName());
                            log.info("The param type is: " + type);
                            arg.setType(type);
                            argList.add(arg);
                        }
                    }
                    bm.setArgumentList(argList);
                    businessMethods.add(bm);
                }
                String rootPackage = simpleUmlPackage.getFullPackageName();
                Session session = new Session(rootPackage);
                session.setName(suc.getName());
                session.setRootPackage(rootPackage);
                session.setRefName(suc.getName());
                session.setDescription(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, suc));
                session.setBusinessMethods(businessMethods);
                // Now add the entity refs to the session.  
                Collection deps = model.getSimpleDependencies();
                for (Iterator iterator = deps.iterator(); iterator.hasNext(); ) {
                    SimpleDependency sd = (SimpleDependency) iterator.next();
                    // session.addRef(entityRef);  
                    if (sd.getClient().getName().equals(session.getName().toString())) {
                        // This dependency is from the current Session. Add a ref.  
                        String entityRef = sd.getSupplier().getName();
                        session.addRef(entityRef);
                    }
                }
                // Put the entity in the hashmap entity  
                map.put(session.getRefName(), session);
            }
        }
    }
    return map;
}

/** Create the session EJBs on the simpleModel. */
private HashMap createSessionEJBs(ArrayList sessionEJBs, SimpleModel simpleModel) {
    HashMap map = new HashMap();
    for (int i = 0; i < sessionEJBs.size(); i++) {
        Session s = (Session) sessionEJBs.get(i);
        String sessionPackage = s.getRootPackage().toString();
        String documentation = s.getDescription().toString();
        String name = s.getName().toString();
        String refName = s.getRefName();
        simpleModel.addSimpleUmlPackage(sessionPackage);
        SimpleUmlPackage pk = simpleModel.addSimpleUmlPackage(sessionPackage);
        // Here the actual UML session should be stored.  
        String sessionStereoType = JagUMLProfile.STEREOTYPE_CLASS_SERVICE;
        SimpleUmlClass sessionClass = new SimpleUmlClass(name, SimpleModelElement.PUBLIC);
        simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_CLASS_SERVICE, sessionClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, documentation, sessionClass);
        pk.addSimpleClassifier(sessionClass);
        ArrayList methods = s.getBusinessMethods();
        if (methods != null) {
            for (int j = 0; j < methods.size(); j++) {
                BusinessMethod bm = (BusinessMethod) methods.get(j);
                SimpleOperation op = new SimpleOperation();
                op.setName(bm.getMethodName());
                String desc = bm.getDescription();
                if (desc == null) {
                    desc = "";
                }
                simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, desc, op);
                // Now add the arguments.  
                ArrayList arguments = bm.getArgumentList();
                if (arguments != null) {
                    for (int k = 0; k < arguments.size(); k++) {
                        BusinessArgument arg = (BusinessArgument) arguments.get(k);
                        SimpleParameter sp = new SimpleParameter();
                        sp.setName(arg.getName());
                        SimpleUmlClass sc = new SimpleUmlClass(arg.getType(), SimpleModelElement.PUBLIC);
                        simpleModel.addSimpleClassifier(sc);
                        sp.setKind(SimpleParameter.IN);
                        sp.setType(sc);
                        op.addSimpleParameter(sp);
                    }
                }
                // And finally add the return type:  
                SimpleParameter sp = new SimpleParameter();
                sp.setName("");
                SimpleUmlClass sc = new SimpleUmlClass(bm.getReturnType(), SimpleModelElement.PUBLIC);
                simpleModel.addSimpleClassifier(sc);
                sp.setKind(SimpleParameter.RETURN);
                sp.setType(sc);
                op.addSimpleParameter(sp);
                sessionClass.addSimpleOperation(op);
            }
        }
        map.put(refName, sessionClass);
    }
    return map;
}

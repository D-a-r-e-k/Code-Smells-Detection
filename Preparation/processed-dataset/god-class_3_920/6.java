public MBeanOperationInfo[] getOperations() {
    int length = 0;
    int pos = 0;
    Method[] meths = iclazz.getMethods();
    //MBeanOperationInfo[] toRet = new MBeanOperationInfo[length]; 
    Vector operInfo = new Vector();
    MBeanOperationInfo mbOperInfo = null;
    for (int i = 0; i < meths.length; i++) {
        String methName = meths[i].getName();
        String returnType = meths[i].getReturnType().getName();
        Class[] mparams = meths[i].getParameterTypes();
        if (methName.startsWith("get") && !methName.equals("get") && (mparams.length == 0) && !(returnType.equals("void"))) {
            continue;
        }
        if (methName.startsWith("is") && !methName.equals("is") && (mparams.length == 0) && (returnType.equals("boolean") || returnType.equals("java.lang.Boolean"))) {
            continue;
        }
        if (methName.startsWith("set") && !methName.equals("set") && (mparams.length == 1) && returnType.equals("void")) {
            continue;
        }
        MBeanParameterInfo[] params = new MBeanParameterInfo[mparams.length];
        //commentSystem.out.println("\n\n** DEFAULT DYNAMIC MBEAN = "+meths[i].getName()); 
        for (int j = 0; j < params.length; j++) {
            //commentSystem.out.println("** PARAMETER = "+mparams[j].getName()); 
            //params[j] = new MBeanParameterInfo( ("param" + j ), convertToJmxArrayType(mparams[j].getName()),"param to method"); 
            params[j] = new MBeanParameterInfo(("param" + j), mparams[j].getName(), "param to method");
        }
        String des = "Operation exposed for management";
        if (clazz.getName().equals("javax.management.loading.MLet")) {
            if (meths[i].getName().equals("addURL"))
                des = "Appends the specified URL to the list of URLs to search for classes and resources.";
            else if (meths[i].getName().equals("getMBeansFromURL"))
                des = "Loads a text file containing MLET tags that define the MBeans to be added to the agent.";
        }
        mbOperInfo = new MBeanOperationInfo(meths[i].getName(), des, params, meths[i].getReturnType().getName(), MBeanOperationInfo.ACTION);
        operInfo.add(mbOperInfo);
    }
    int size = operInfo.size();
    MBeanOperationInfo[] toRet = new MBeanOperationInfo[size];
    for (int i = 0; i < size; i++) {
        toRet[i] = (MBeanOperationInfo) operInfo.get(i);
    }
    return toRet;
}

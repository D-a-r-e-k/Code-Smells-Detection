public Object invoke(String actionName, Object[] params, String[] signature) throws javax.management.MBeanException, javax.management.ReflectionException {
    Class e = null;
    try {
        e = object.getClass();
        MBeanOperationInfo[] opers = mbeanInfo.getOperations();
        MBeanOperationInfo oper = null;
        for (int i = 0; i < opers.length; i++) {
            if (opers[i].getName().equals(actionName)) {
                MBeanParameterInfo[] pars = opers[i].getSignature();
                if (pars == null)
                    break;
                if (params != null) {
                    if (!(pars.length == params.length)) {
                        if (i != (opers.length - 1)) {
                            continue;
                        } else {
                            throw new RuntimeOperationsException(new IllegalArgumentException());
                        }
                    }
                }
                if (params != null && signature != null) {
                    if (params.length != signature.length) {
                        throw new ReflectionException(new IllegalArgumentException());
                    }
                }
                int j = 0;
                for (; j < pars.length; j++) {
                    if (params[j] != null) {
                        String jmxParam = convertToJmxArrayType(signature[j]);
                        if (!pars[j].getType().equals(jmxParam)) {
                            String temp = convertToWrapperType(pars[j].getType());
                            if ((temp == null) || (!(temp.equals(jmxParam))))
                                continue;
                        }
                    }
                }
                if (j < pars.length)
                    continue;
                oper = opers[i];
                break;
            }
        }
        if (oper == null) {
            /* Provision for handling Attribute */
            /*if(actionName.startsWith("get") || actionName.startsWith("set")){
                  actionName = actionName.substring(3);
                  }
                  else if(actionName.startsWith("is")){
                  actionName = actionName.substring(2);
                  }
                  Object attribute = getAttribute(actionName);
                  if(attribute == null){
                  throw new MBeanException(new Exception());
                  }*/
            /** This is to execute Attributes .Setter will have only one parameters*/
            Class[] paramClass = null;
            if (params != null) {
                if (params.length == 0 || params.length == 1) {
                    paramClass = new Class[params.length];
                    for (int i = 0; i < params.length; i++) {
                        paramClass[i] = params[i].getClass();
                    }
                } else {
                    //throw new MBeanException(new Exception()); 
                    throw new RuntimeOperationsException(new IllegalArgumentException());
                }
            }
            Method attrMethod = null;
            try {
                attrMethod = e.getMethod(actionName, paramClass);
            } catch (NoSuchMethodException ne) {
                if (paramClass.length == 1) {
                    Method[] meths = e.getMethods();
                    for (int i = 0; i < meths.length; i++) {
                        if (meths[i].getName().equals(actionName)) {
                            Class[] parameters = meths[i].getParameterTypes();
                            if (parameters[0].isAssignableFrom(paramClass[0])) {
                                attrMethod = meths[i];
                                return attrMethod.invoke(object, params);
                            }
                        }
                    }
                    if (attrMethod == null) {
                        paramClass[0] = getPrimitiveClass(paramClass[0].getName());
                        attrMethod = e.getMethod(actionName, paramClass);
                    }
                } else {
                    throw new MBeanException(new Exception());
                }
            }
            if (attrMethod == null) {
                throw new MBeanException(new Exception());
            }
            return attrMethod.invoke(object, params);
        }
        MBeanParameterInfo[] pars = oper.getSignature();
        Class parameterTypes[] = null;
        if (params != null) {
            parameterTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                Class clazz = getProperClass(pars[i].getType());
                if (clazz == null)
                    clazz = Thread.currentThread().getContextClassLoader().loadClass(signature[i]);
                parameterTypes[i] = clazz;
            }
        }
        Method attrMethod = e.getMethod(actionName, parameterTypes);
        return attrMethod.invoke(object, params);
    } catch (ReflectionException re) {
        throw re;
    } catch (Exception ex) {
        if (ex instanceof InvocationTargetException) {
            /** For TCK */
            Exception ee = null;
            InvocationTargetException ite = (InvocationTargetException) ex;
            Throwable th = ite.getTargetException();
            if (th instanceof Error) {
                Error er = (Error) th;
                RuntimeErrorException ree = new RuntimeErrorException(er);
                throw ree;
            } else {
                //ee = (Exception)th; 
                if (th instanceof RuntimeException) {
                    throw new RuntimeMBeanException((RuntimeException) th);
                }
                if (th instanceof ReflectionException) {
                    throw new MBeanException((ReflectionException) th);
                }
            }
            throw new MBeanException((Exception) th);
        }
        if (ex instanceof RuntimeOperationsException) {
            throw (RuntimeOperationsException) ex;
        }
        if (ex instanceof RuntimeException) {
            throw new RuntimeMBeanException((RuntimeException) ex);
        }
        if (ex instanceof ClassNotFoundException) {
            try {
                Class[] classParams = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    if (isPrimitiveDataType(signature[i]))
                        classParams[i] = getProperClass(signature[i]);
                    else
                        classParams[i] = params[i].getClass();
                }
                //try{ 
                Method meth = e.getMethod(actionName, classParams);
                return meth.invoke(object, params);
            } catch (NoSuchMethodException ne) {
                try {
                    Class[] cParams = new Class[params.length];
                    for (int i = 0; i < params.length; i++) {
                        try {
                            cParams[i] = DefaultLoaderRepositoryExt.loadClass(signature[i]);
                        } catch (Exception exc) {
                            //when classnotfound in default loader repository, there is a chance it to be 
                            //system class like java.* 
                            if (isPrimitiveDataType(signature[i]))
                                cParams[i] = getProperClass(signature[i]);
                            else
                                cParams[i] = Thread.currentThread().getContextClassLoader().loadClass(signature[i]);
                        }
                    }
                    Method meth = e.getMethod(actionName, cParams);
                    return meth.invoke(object, params);
                } catch (NoSuchMethodException nee) {
                    throw new ReflectionException(ne);
                } catch (Exception eee) {
                    throw new MBeanException(eee);
                }
            } catch (Exception ene) {
                if (ene instanceof MBeanException) {
                    throw (MBeanException) ene;
                }
                throw new MBeanException(ene);
            }
        }
        throw new MBeanException(ex);
    }
}

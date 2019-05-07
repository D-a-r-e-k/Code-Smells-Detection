public void setAttribute(Attribute attribute) throws javax.management.AttributeNotFoundException, javax.management.InvalidAttributeValueException, javax.management.MBeanException, javax.management.ReflectionException {
    if (attribute == null)
        throw new RuntimeOperationsException(new IllegalArgumentException("Null values not possible"));
    try {
        Class e = null;
        try {
            e = Thread.currentThread().getContextClassLoader().loadClass(mbeanInfo.getClassName());
        } catch (ClassNotFoundException cce) {
            MBeanServerImpl server = null;
            ArrayList list = MBeanServerFactory.findMBeanServer(null);
            if (list != null)
                server = (MBeanServerImpl) list.toArray()[0];
            else
                throw cce;
            Object loader = server.getLoaderTable().get(mbeanInfo.getClassName());
            if (loader == null)
                throw cce;
            e = Class.forName(mbeanInfo.getClassName(), true, (ClassLoader) loader);
        }
        MBeanAttributeInfo[] attrs = mbeanInfo.getAttributes();
        MBeanAttributeInfo attr = null;
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i].getName().equals(attribute.getName())) {
                attr = attrs[i];
                break;
            }
        }
        if (attr == null)
            throw new AttributeNotFoundException(attribute.getName());
        Class parameterTypes[] = new Class[1];
        Class clazz = getProperClass(attr.getType());
        if (clazz == null) {
            clazz = attribute.getValue().getClass();
        }
        parameterTypes[0] = clazz;
        Method attrMethod = null;
        ArrayList methodList = new ArrayList();
        ArrayList paramList = new ArrayList();
        try {
            Method[] meths = e.getMethods();
            Class[] params = null;
            parameterTypes[0] = null;
            for (int i = 0; i < meths.length; i++) {
                Method meth = meths[i];
                if (meth.getName().equals("set" + attribute.getName())) {
                    params = meth.getParameterTypes();
                    //Attribute will have only one param 
                    if (params != null) {
                        if (params[0].isAssignableFrom(clazz)) {
                            parameterTypes[0] = params[0];
                            methodList.add(meth);
                            paramList.add(parameterTypes[0]);
                        }
                    }
                }
                if (params != null && params.length > 0 && params[0] != null) {
                    if (parameterTypes[0] == null) {
                        throw new InvalidAttributeValueException();
                    }
                }
            }
            int size = methodList.size();
            if (size == 0) {
                throw new NoSuchMethodException();
            } else if (size == 1) {
                attrMethod = (Method) methodList.get(0);
            } else {
                MBeanInfo info = getMBeanInfo();
                //String className = info.getClassName()+"MBean"; 
                //Class mbeanClass = Class.forName(className); 
                Method[] mbeanMeths = iclazz.getMethods();
                int mbeanMethsSize = mbeanMeths.length;
                boolean isFound = false;
                for (int i = 0; i < size; i++) {
                    Method meth = (Method) methodList.get(i);
                    for (int j = 0; j < mbeanMethsSize; j++) {
                        if (meth.getName().equals(mbeanMeths[j].getName())) {
                            if (Arrays.equals(meth.getExceptionTypes(), mbeanMeths[j].getExceptionTypes())) {
                                attrMethod = meth;
                                isFound = true;
                                break;
                            }
                        }
                    }
                    if (isFound) {
                        break;
                    }
                }
            }
            if (attrMethod == null) {
                throw new NoSuchMethodException();
            }
        } catch (Exception ee) {
            if (ee instanceof InvalidAttributeValueException) {
                throw (InvalidAttributeValueException) ee;
            }
            if (ee instanceof NoSuchMethodException) {
                throw new AttributeNotFoundException();
            }
            //ee.printStackTrace(); 
            throw new MBeanException(ee);
        }
        try {
            attrMethod.invoke(object, new Object[] { attribute.getValue() });
        } catch (Exception e2) {
            if (e2 instanceof AttributeNotFoundException) {
                throw (AttributeNotFoundException) e2;
            }
            if (e2 instanceof InvocationTargetException) {
                /** For TCK */
                InvocationTargetException ite = (InvocationTargetException) e2;
                Throwable th = ite.getTargetException();
                if (th instanceof Error) {
                    Error er = (Error) th;
                    RuntimeErrorException ree = new RuntimeErrorException(er);
                    throw ree;
                } else {
                    if (th instanceof ReflectionException) {
                        throw (ReflectionException) th;
                    }
                    if (th instanceof RuntimeException) {
                        throw new RuntimeMBeanException((RuntimeException) th);
                    } else {
                        throw new MBeanException((Exception) th);
                    }
                }
            }
        }
    } catch (ReflectionException e) {
        throw e;
    } catch (Exception e) {
        if (e instanceof InvalidAttributeValueException) {
            throw (InvalidAttributeValueException) e;
        }
        if (e instanceof AttributeNotFoundException) {
            throw (AttributeNotFoundException) e;
        }
        if (e instanceof InvocationTargetException) {
            /** For TCK */
            InvocationTargetException ite = (InvocationTargetException) e;
            Throwable th = ite.getTargetException();
            if (th instanceof Error) {
                Error er = (Error) th;
                RuntimeErrorException ree = new RuntimeErrorException(er);
                throw ree;
            } else {
                if (th instanceof ReflectionException) {
                    throw (ReflectionException) th;
                }
                if (th instanceof RuntimeException) {
                    throw new RuntimeMBeanException((RuntimeException) th);
                } else {
                    throw new MBeanException((Exception) th);
                }
            }
        }
        if (e instanceof RuntimeMBeanException) {
            throw (RuntimeMBeanException) e;
        }
        if (e instanceof MBeanException) {
            throw (MBeanException) e;
        }
    }
}

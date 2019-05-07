public Object getAttribute(String attribute) throws javax.management.AttributeNotFoundException, javax.management.MBeanException, javax.management.ReflectionException {
    try {
        Class e = null;
        e = object.getClass();
        Method attrMethod = null;
        Object[] arguments = new Object[0];
        try {
            //String[] attributes = getAttributes(); 
            MBeanAttributeInfo[] attrInfo = getMBeanInfo().getAttributes();
            boolean isAttributeFound = false;
            for (int i = 0; i < attrInfo.length; i++) {
                if (attribute.equals(attrInfo[i].getName())) {
                    isAttributeFound = true;
                }
            }
            if (!isAttributeFound) {
                throw new AttributeNotFoundException();
            }
            attrMethod = e.getMethod("get" + attribute, null);
            /*if(attrMethod == null){
                        throw new AttributeNotFoundException();
                }*/
            return attrMethod.invoke(object, arguments);
        } catch (Exception ee) {
            /** For TCK */
            if (ee instanceof NoSuchMethodException) {
                attrMethod = e.getMethod("is" + attribute, null);
                return attrMethod.invoke(object, arguments);
            }
            throw ee;
        }
    } catch (Exception e) {
        if (e instanceof AttributeNotFoundException) {
            throw (AttributeNotFoundException) e;
        }
        if (e instanceof NoSuchMethodException)
            throw new AttributeNotFoundException();
        if (e instanceof InvocationTargetException) {
            /** For TCK */
            InvocationTargetException ite = (InvocationTargetException) e;
            Throwable th = ite.getTargetException();
            if (th instanceof Error) {
                Error er = (Error) th;
                RuntimeErrorException ree = new RuntimeErrorException(er);
                throw ree;
            }
            Exception e1 = (Exception) ((InvocationTargetException) e).getTargetException();
            throw new ReflectionException(e1);
        }
        throw new ReflectionException(e);
    }
}

/**
     *  Gets an attribute using reflection from the MBean.
     *  
     *  @param name Name of the attribute to find.
     *  @return The value returned by the corresponding getXXX() call
     *  @throws AttributeNotFoundException If there is not such attribute
     *  @throws MBeanException 
     *  @throws ReflectionException
     */
public Object getAttribute(String name) throws AttributeNotFoundException, MBeanException, ReflectionException {
    Method m;
    Object res = null;
    try {
        String mname = "get" + StringUtils.capitalize(name);
        m = findGetterSetter(getClass(), mname, null);
        if (m == null)
            throw new AttributeNotFoundException(name);
        res = m.invoke(this, (Object[]) null);
    } catch (SecurityException e) {
        // TODO Auto-generated catch block 
        e.printStackTrace();
    } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block 
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block 
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        // TODO Auto-generated catch block 
        e.printStackTrace();
    }
    return res;
}

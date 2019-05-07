/**
     *  Invokes a particular method.
     *  
     *  @param arg0 Method name
     *  @param arg1 A list of arguments for the invocation
     */
public Object invoke(String arg0, Object[] arg1, String[] arg2) throws MBeanException, ReflectionException {
    Method[] methods = getClass().getMethods();
    for (int i = 0; i < methods.length; i++) {
        if (methods[i].getName().equals(arg0)) {
            try {
                return methods[i].invoke(this, arg1);
            } catch (IllegalArgumentException e) {
                throw new ReflectionException(e, "Wrong arguments");
            } catch (IllegalAccessException e) {
                throw new ReflectionException(e, "No access");
            } catch (InvocationTargetException e) {
                throw new ReflectionException(e, "Wrong target");
            }
        }
    }
    throw new ReflectionException(null, "There is no such method " + arg0);
}

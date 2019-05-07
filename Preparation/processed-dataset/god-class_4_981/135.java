/**
     * <P>
     * Get the driver delegate for DB operations.
     * </p>
     */
protected DriverDelegate getDelegate() throws NoSuchDelegateException {
    if (null == delegate) {
        try {
            if (delegateClassName != null) {
                delegateClass = getClassLoadHelper().loadClass(delegateClassName);
            }
            Constructor ctor = null;
            Object[] ctorParams = null;
            if (canUseProperties()) {
                Class[] ctorParamTypes = new Class[] { Logger.class, String.class, String.class, Boolean.class };
                ctor = delegateClass.getConstructor(ctorParamTypes);
                ctorParams = new Object[] { getLog(), tablePrefix, instanceId, new Boolean(canUseProperties()) };
            } else {
                Class[] ctorParamTypes = new Class[] { Logger.class, String.class, String.class };
                ctor = delegateClass.getConstructor(ctorParamTypes);
                ctorParams = new Object[] { getLog(), tablePrefix, instanceId };
            }
            delegate = (DriverDelegate) ctor.newInstance(ctorParams);
        } catch (NoSuchMethodException e) {
            throw new NoSuchDelegateException("Couldn't find delegate constructor: " + e.getMessage());
        } catch (InstantiationException e) {
            throw new NoSuchDelegateException("Couldn't create delegate: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new NoSuchDelegateException("Couldn't create delegate: " + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new NoSuchDelegateException("Couldn't create delegate: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new NoSuchDelegateException("Couldn't load delegate class: " + e.getMessage());
        }
    }
    return delegate;
}

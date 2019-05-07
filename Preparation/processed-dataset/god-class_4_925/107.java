public SecurityManager getSecurityManager() throws AppException {
    if (getSecurityManagerClass() == null)
        return null;
    SecurityManager sm = null;
    try {
        sm = (SecurityManager) getClass(getSecurityManagerClass(), true).newInstance();
    } catch (ClassNotFoundException e) {
        throw new AppException(e.getMessage());
    } catch (InstantiationException e) {
        throw new AppException(e.getMessage());
    } catch (IllegalAccessException e) {
        throw new AppException(e.getMessage());
    }
    return sm;
}

public void setAttribute(Attribute attr) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
    Method m;
    String mname = "set" + StringUtils.capitalize(attr.getName());
    m = findGetterSetter(getClass(), mname, attr.getValue().getClass());
    if (m == null)
        throw new AttributeNotFoundException(attr.getName());
    Object[] args = { attr.getValue() };
    try {
        m.invoke(this, args);
    } catch (IllegalArgumentException e) {
        throw new InvalidAttributeValueException("Faulty argument: " + e.getMessage());
    } catch (IllegalAccessException e) {
        throw new ReflectionException(e, "Cannot access attribute " + e.getMessage());
    } catch (InvocationTargetException e) {
        throw new ReflectionException(e, "Cannot invoke attribute " + e.getMessage());
    }
}

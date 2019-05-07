protected void makeMBeanInfo() throws Exception {
    String className = clazz.getName();
    String description = className + " MBean";
    mbeanInfo = new MBeanInfo(className, description, getAttributes(), getConstructors(), getOperations(), getNotifications());
}

private boolean getFlagValue(Class mainClass, boolean value) {
    clazz = mainClass.getSuperclass();
    Class[] interfaces = clazz.getInterfaces();
    for (int i = 0; i < interfaces.length; i++) {
        if (interfaces[i].getName().equals(clazz.getName() + "MBean")) {
            iclazz = interfaces[i];
            value = true;
            break;
        }
    }
    return value;
}

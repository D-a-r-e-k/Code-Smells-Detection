public DefaultDynamicMBean(Object object) throws Exception {
    this.object = object;
    clazz = object.getClass();
    Class[] interfaces = clazz.getInterfaces();
    boolean flag = false;
    if (interfaces == null) {
        clazz = clazz.getSuperclass();
        if (clazz == null)
            throw new Exception("NonJmxMBeanRegistrationException");
        if ((interfaces = clazz.getInterfaces()) == null)
            throw new Exception("NonJmxMBeanRegistrationException");
    } else {
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].getName().equals(clazz.getName() + "MBean")) {
                iclazz = interfaces[i];
                flag = true;
                /** This will be used for Attribute's get, set and is methods overloading */
                Class[] interClass = interfaces[i].getInterfaces();
                if (interClass != null) {
                    if (interClass.length > 0) {
                        superInterfaceMeths = interClass[0].getMethods();
                    }
                }
                break;
            }
        }
        if (!flag) {
            clazz = clazz.getSuperclass();
            if (clazz == null)
                throw new Exception("NonJmxMBeanRegistrationException");
            if ((interfaces = clazz.getInterfaces()) == null)
                throw new Exception("NonJmxMBeanRegistrationException");
        }
    }
    if (!flag) {
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].getName().equals(clazz.getName() + "MBean")) {
                iclazz = interfaces[i];
                flag = true;
                break;
            }
        }
        while (!flag) {
            flag = getFlagValue(clazz, flag);
            if (clazz == null)
                break;
        }
    }
    if (!flag)
        throw new Exception("NonJmxMBeanRegistrationException");
    clazz = object.getClass();
    makeMBeanInfo();
}

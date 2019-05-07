//IMplementing for MBeanRegistration 
public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
    if (!(object instanceof MBeanRegistration))
        return null;
    return ((MBeanRegistration) object).preRegister(server, name);
}

public void preDeregister() throws Exception {
    if (!(object instanceof MBeanRegistration))
        return;
    try {
        ((MBeanRegistration) object).preDeregister();
    } catch (Exception e) {
        throw e;
    }
}

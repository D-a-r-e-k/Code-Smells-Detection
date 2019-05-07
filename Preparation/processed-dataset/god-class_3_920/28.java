public void postDeregister() {
    if (!(object instanceof MBeanRegistration))
        return;
    ((MBeanRegistration) object).postDeregister();
}
